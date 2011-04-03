package com.example.glscrollsample;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

public class MyRenderer implements GLSurfaceView.Renderer  {

	private Icon icon;
	private Context context;
	
	public float ypos;
	private final boolean texOES = false;
	private long oneSecondTimerStart = 0;
	private long now;
	protected long drawTimerStart = 0;
	protected long drawTimeDuration = 0;
	private int fpsCounter = 0;

	public MyRenderer(Context context) {
		this.context = context;
		icon = new Icon();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		icon.loadGLTexture(gl, this.context);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		drawTimerStart = System.nanoTime();
	}

	public void onDrawFrame(GL10 gl) {
		fpsCounter++;
		if (oneSecondTimerStart == 0)
			oneSecondTimerStart = System.nanoTime();
		drawTimerStart = System.nanoTime();
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, -ypos, -0.1f);
		
        //Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CW);
		
		for (int j = 1; j < 25; j++) {
			for (int i = 1; i < 5; i++) {
				if (texOES)
					icon.drawOES(gl, (float)(120*i), (float)(-120*j) + -ypos + 800, i*j);
				else {
					icon.draw(gl, (float)(96*i), (float)(-96*j), i*j);
				}
			}
		}
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		drawTimeDuration += System.nanoTime() - drawTimerStart;
		now = System.nanoTime();
		if (now - 1000000000 > oneSecondTimerStart) {
			Log.i("########", String.format("Draw Rate = %f; FPS = %f", 
					(float)drawTimeDuration/(float)(now-oneSecondTimerStart),
					(float)fpsCounter/(float)(now-oneSecondTimerStart)*1000000000));
			oneSecondTimerStart = 0;
			drawTimeDuration = 0;
			drawTimerStart = now;
			fpsCounter = 0;
		}
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) {
			height = 1;
		}
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

//		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
		GLU.gluOrtho2D(gl, 0, (float)width, -(float)height, 0);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}
