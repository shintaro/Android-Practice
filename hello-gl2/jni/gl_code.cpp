/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// OpenGL ES 2.0 code

#include <jni.h>
#include <android/log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <GLES/gl.h>
#include <GLES/glext.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define  LOG_TAG    "libgl2jni"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

static GLuint texBuff[64][64][4];
static GLuint textures[1];

void loadTexture( void ){
	//　テクスチャロードが面倒なんで捏造
	int i,j;
	for( i=0 ; i<64 ; i++ ){
		for( j=0 ; j<64 ; j++ ){
			if( i < 32 ){
				texBuff[ i ][ j ][ 0 ] = 0xff;	// R
				texBuff[ i ][ j ][ 1 ] = 0x00;	// G
				texBuff[ i ][ j ][ 2 ] = 0x00;	// B
				texBuff[ i ][ j ][ 3 ] = 0xff;	// A
			} else {
				texBuff[ i ][ j ][ 0 ] = 0x00;	// R
				texBuff[ i ][ j ][ 1 ] = 0x00;	// G
				texBuff[ i ][ j ][ 2 ] = 0xff;	// B
				texBuff[ i ][ j ][ 3 ] = 0xff;	// A
			}
		}
	}

	glGenTextures( 1, &textures[0] );
	glBindTexture( GL_TEXTURE_2D, textures[0] );

	glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST );	//　縮小するときピクセルの中心に最も近いテクスチャ要素で補完
	glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );	//　拡大するときピクセルの中心付近の線形で補完
	glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );	//　s座標の1を超える端処理をループにしない
	glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );	//　t座標の1を超える端処理をループにしない
	
	glTexEnvf( GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE );	//　テクスチャとモデルの合成方法の指定（この場合置き換え）
	
	glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA, 64, 64, 0, GL_RGBA, GL_UNSIGNED_BYTE, texBuff );
//	glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA, 64, 64, 0, GL_RGBA, GL_UNSIGNED_SHORT, texBuff );
}

static void printGLString(const char *name, GLenum s) {
    const char *v = (const char *) glGetString(s);
    LOGI("GL %s = %s\n", name, v);
}

static void checkGlError(const char* op) {
    for (GLint error = glGetError(); error; error
            = glGetError()) {
        LOGI("after %s() glError (0x%x)\n", op, error);
    }
}

static const char gVertexShader[] = 
    "attribute vec4 vPosition;\n"
    "void main() {\n"
    "  gl_Position = vPosition;\n"
    "}\n";

static const char gFragmentShader[] = 
    "precision mediump float;\n"
    "void main() {\n"
    "  gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n"
    "}\n";

GLuint loadShader(GLenum shaderType, const char* pSource) {
    GLuint shader = glCreateShader(shaderType);
    if (shader) {
        glShaderSource(shader, 1, &pSource, NULL);
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
        if (!compiled) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen) {
                char* buf = (char*) malloc(infoLen);
                if (buf) {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
                    LOGE("Could not compile shader %d:\n%s\n",
                            shaderType, buf);
                    free(buf);
                }
                glDeleteShader(shader);
                shader = 0;
            }
        }
    }
    return shader;
}

GLuint createProgram(const char* pVertexSource, const char* pFragmentSource) {
    GLuint vertexShader = loadShader(GL_VERTEX_SHADER, pVertexSource);
    if (!vertexShader) {
        return 0;
    }

    GLuint pixelShader = loadShader(GL_FRAGMENT_SHADER, pFragmentSource);
    if (!pixelShader) {
        return 0;
    }

    GLuint program = glCreateProgram();
    if (program) {
        glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        glAttachShader(program, pixelShader);
        checkGlError("glAttachShader");
        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength) {
                char* buf = (char*) malloc(bufLength);
                if (buf) {
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    LOGE("Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            glDeleteProgram(program);
            program = 0;
        }
    }
    return program;
}

GLuint gProgram;
GLuint gvPositionHandle;

bool setupGraphics(int w, int h) {
    printGLString("Version", GL_VERSION);
    printGLString("Vendor", GL_VENDOR);
    printGLString("Renderer", GL_RENDERER);
    printGLString("Extensions", GL_EXTENSIONS);

    LOGI("setupGraphics(%d, %d)", w, h);


    gProgram = createProgram(gVertexShader, gFragmentShader);
    if (!gProgram) {
        LOGE("Could not create program.");
        return false;
    }
    gvPositionHandle = glGetAttribLocation(gProgram, "vPosition");
    checkGlError("glGetAttribLocation");
    LOGI("glGetAttribLocation(\"vPosition\") = %d\n",
            gvPositionHandle);

    glViewport(0, 0, w, h);
    checkGlError("glViewport");
/*
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
//    glOrthof(-(float)w/2, (float)w/2, -(float)h/2, (float)h/2, 2.0f, -2.0f);
    glFrustumf(0, (float)w, -(float)h, 0, 2.0f, -2.0f);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
*/
    loadTexture();

    return true;
}

const GLfloat gTriangleVertices[] = { 0.0f, 0.5f, -0.5f, -0.5f,
        0.5f, -0.5f };

/*
const GLfloat vertices[] = {
        -1.0,  1.0, -0.0,
        -1.0, -1.0, -0.0,
        1.0, 1.0, -0.0,
         1.0, -1.0, -0.0
    };
*/

const GLfloat vertices[] = {
        -100, 100, 
        -100, -100, 
        100, 100, 
        100, -100 
    };
/*
const GLfloat vertices[] = {
        -0.1, 0.1, 
        -0.1, -0.1, 
        0.1, 0.1, 
        0.1, -0.1 
    };
*/

void renderFrame(int w, int h, int ypos) {

    static float grey;
    grey += 0.005f;
    if (grey > 1.0f) {
        grey = 0.0f;
    }

    glClearColor(grey, grey, grey, 1.0f);
    checkGlError("glClearColor");
    glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    checkGlError("glClear");

    glUseProgram(gProgram);
    checkGlError("glUseProgram");

//   glScalef(1.0f/240.0f, 1.0f/400.0f, 0);
/*
    glVertexAttribPointer(gvPositionHandle, 2, GL_FLOAT, GL_FALSE, 0, gTriangleVertices);
    checkGlError("glVertexAttribPointer");
    glEnableVertexAttribArray(gvPositionHandle);
    checkGlError("glEnableVertexAttribArray");
    glDrawArrays(GL_TRIANGLES, 0, 3);
    checkGlError("glDrawArrays");
*/
    //glEnableClientState (GL_VERTEX_ARRAY);
    //glVertexPointer (3, GL_BYTE , 0, vertices);	


    LOGI("ypos = %d", ypos);

//    glTranslatex(0,(GLfixed)(ypos / 400.0f),0);
//    checkGlError("glTranslatex");

    GLfloat vert[8];


    for (int j = 1; j < 30; j+=2) {
        for (int i = 1; i < 9; i+=2) {
            vert[0] = -1.0 + (2.0/9)*i;
            vert[1] = 1.0 +  - ((2.0/9.0f) * 480.0f/800.0f)*j;
  
            vert[2] = vert[0];
            vert[3] = vert[1] - ((2.0/9) * 480/800);

            vert[4] = vert[0] + (2.0/9);
            vert[5] = vert[1];

            vert[6] = vert[4];
            vert[7] = vert[3];

/*
    for (int j = 480/4; j > -1130; j-=480/4) {
        for (int i = 480/4; i < 480; i+=480/4) {
            vert[0] = i - 32;
            vert[1] = ypos + j + 32;

            vert[2] = vert[0];
            vert[3] = ypos + j - 32;

            vert[4] = i + 32;
            vert[5] = vert[1];

            vert[6] = vert[4];
            vert[7] = vert[3];
*/

        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        checkGlError("glColor");


glPushMatrix();
glEnable(GL_TEXTURE_2D);
glBindTexture(GL_TEXTURE_2D, textures[0]);
int rect[4] = {0, 128, 128, -128};
// バインドされているテクスチャのどの部分を使うかを指定
glTexParameteriv(GL_TEXTURE_2D,
    GL_TEXTURE_CROP_RECT_OES, rect);
glDrawTexfOES(50.0f, 100.0f, 0, 128.0f, 128.0f );

            glVertexAttribPointer(gvPositionHandle, 2, GL_FLOAT, GL_FALSE, 0, vert);
            checkGlError("glVertexAttribPointer");
            glEnableVertexAttribArray(gvPositionHandle);
            checkGlError("glEnableVertexAttribArray");
            glDrawArrays (GL_TRIANGLE_STRIP, 0, 4);
            checkGlError("glDrawArrays");

glDisable(GL_TEXTURE_2D);
glPopMatrix();
        }
    }
}

extern "C" {
    JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNILib_init(JNIEnv * env, jobject obj,  jint width, jint height);
    JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNILib_step(JNIEnv * env, jobject obj, jint width, jint height, jint ypos);
};

JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNILib_init(JNIEnv * env, jobject obj,  jint width, jint height)
{
    setupGraphics(width, height);
}

JNIEXPORT void JNICALL Java_com_android_gl2jni_GL2JNILib_step(JNIEnv * env, jobject obj, jint width, jint height, jint ypos)
{
    renderFrame(width, height, ypos);
}
