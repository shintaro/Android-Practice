package com.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MonkyTest extends Activity {
	
	static long lastTime;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent event) {
       	if (event.getAction() == MotionEvent.ACTION_DOWN) {   	        
   	        long currentTime = System.nanoTime();
//   	    System.currentTImeMillis();    
       	    long erapsedTime = currentTime - lastTime;
   	        lastTime = currentTime;
   	   	    Log.v("XXXXXXXXXXXXXXXXXXXXXX", Long.toString(erapsedTime));
       	}
   	 
        return super.onTouchEvent(event); 
    }    
}