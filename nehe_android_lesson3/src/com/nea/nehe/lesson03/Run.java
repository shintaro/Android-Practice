package com.nea.nehe.lesson03;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

/**
 * The initial Android Activity, setting and initiating
 * the OpenGL ES Renderer Class @see Lesson03.java
 * 
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class Run extends Activity {

	/** The OpenGL View */
	private GLSurfaceView glSurface;
	private Lesson03 l3;
	static private float first;
	static private float y;

	/**
	 * Initiate the OpenGL View and set our own
	 * Renderer (@see Lesson03.java)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Create an Instance with this Activity
		glSurface = new GLSurfaceView(this);
		l3 = new Lesson03(this);
		//Set our own Renderer
		glSurface.setRenderer(l3);
		//Set the GLSurface as View to this Activity
		setContentView(glSurface);
	}

	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();
		glSurface.onResume();
	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		super.onPause();
		glSurface.onPause();
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_DOWN)
    		first = (int)event.getY();

    	if (event.getAction() == MotionEvent.ACTION_MOVE)
       		l3.ypos = y + first - (int)event.getY();

       	if (event.getAction() == MotionEvent.ACTION_UP)
       		y = l3.ypos;
       	
       	//Log.v("Move Amount", Integer.toString(mView.ypos));
    	
        return super.onTouchEvent(event);
    }
}