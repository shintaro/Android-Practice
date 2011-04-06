package com.android.eventcounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class EventCounter extends Activity {
    /** Called when the activity is first created. */
    private TextView tv;
    private int counter;
	private long start, now;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv = (TextView)findViewById(R.id.textView1);
        tv.setText("Event/Sec = ");
        start = System.currentTimeMillis();
        counter = 0;
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
  
    	counter++;
    	now = System.currentTimeMillis();
    	if (now - start > 1000) {
            tv.setText(String.format("Event/Sec = %f", (float)counter * (float)1000 / (float)(now - start)));
            start = now;
            counter = 0;
    	}
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {

    	}
    	
    	if (event.getAction() == MotionEvent.ACTION_MOVE) {

    	}
    	
    	if (event.getAction() == MotionEvent.ACTION_UP) {

    	}
       
       return true;
    }
}