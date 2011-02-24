package com.nea.nehe.lesson03;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

/**
 * This is a port of the {@link http://nehe.gamedev.net} OpenGL 
 * tutorials to the Android 1.5 OpenGL ES platform. Thanks to 
 * NeHe and all contributors for their great tutorials and great 
 * documentation. This source should be used together with the
 * textual explanations made at {@link http://nehe.gamedev.net}.
 * The code is based on the original Visual C++ code with all
 * comments made. It has been altered and extended to meet the
 * Android requirements. The Java code has according comments.
 * 
 * If you use this code or find it helpful, please visit and send
 * a shout to the author under {@link http://www.insanitydesign.com/}
 * 
 * @DISCLAIMER
 * This source and the whole package comes without warranty. It may or may
 * not harm your computer or cell phone. Please use with care. Any damage
 * cannot be related back to the author. The source has been tested on a
 * virtual environment and scanned for viruses and has passed all tests.
 * 
 * 
 * This is an interpretation of "Lesson 03: Adding Color"
 * for the Google Android platform.
 * 
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class Lesson03 implements Renderer {
	
	private Square square;
	private Context context;
	
	public float ypos;
	private final boolean texOES = false;
	private long lastTimeMillis;
	
	/**
	 * Instance the Triangle and Square objects
	 */
	public Lesson03(Context context) {
		this.context = context;
		square = new Square();
		lastTimeMillis = System.currentTimeMillis();
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		square.loadGLTexture(gl, this.context);
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.5f, 0.5f, 0.5f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}

	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) {
		
//		if (lastTimeMillis + 1000/60 < System.currentTimeMillis())
//			return;
		
		lastTimeMillis = System.currentTimeMillis();
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		//Drawing
		gl.glTranslatef(0.0f, ypos*1.5f, -0.1f);	//Move down 1.2 Unit And Into The Screen 6.0

		
		for (int j = 1; j < 25; j++)
			for (int i = 1; i < 5; i++) {
				if (texOES)
					square.drawOES(gl, (float)(96*i), (float)(-96*j) + ypos*1.5f + 800, i*j);
				else
					square.draw(gl, (float)(96*i), (float)(-96*j), i*j);						//Draw the square
			}
		
		//gl.glTranslatef(0.0f, 2.5f, 0.0f);		//Move up 2.5 Units
		//triangle.draw(gl);						//Draw the triangle		
	}

	/**
	 * If the surface changes, reset the view
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}
		
		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
//		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
		GLU.gluOrtho2D(gl, 0, (float)width*480.0f/320.0f, -(float)height*480.0f/320.0f, 0);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
	}
}
