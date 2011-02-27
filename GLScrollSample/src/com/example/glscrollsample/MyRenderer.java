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
	private long lastTimeMillis;
	private int fpsCounter = 0;

	public MyRenderer(Context context) {
		this.context = context;
		icon = new Icon();
		lastTimeMillis = System.currentTimeMillis();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		icon.loadGLTexture(gl, this.context);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}

	public void onDrawFrame(GL10 gl) {
		
		long now = System.currentTimeMillis();
		if (lastTimeMillis + 1000 < now) {
			Log.i("****************", String.format("FPS = %f", (float)fpsCounter * 1000.0f / (float)(now-lastTimeMillis)));
			fpsCounter = 0;
			lastTimeMillis = now;
		}
		
		fpsCounter++;

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();
		
		gl.glTranslatef(0.0f, ypos, -0.1f);

		for (int j = 1; j < 25; j++)
			for (int i = 1; i < 5; i++) {
				if (texOES)
					icon.drawOES(gl, (float)(96*i), (float)(-96*j) + ypos + 800, i*j);
				else
					icon.draw(gl, (float)(96*i), (float)(-96*j), i*j);
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
