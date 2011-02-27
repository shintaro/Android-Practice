package com.example.checktoucheventresponce;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class CheckTouchEventResponce extends Activity {

	private long fireTime, catchTime;
	ArrayList<String> longList = new ArrayList<String>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
        
    	new Thread(new Runnable() {
    		public void run() {
    			while (true) {
    				fireTime = System.nanoTime();
    				System.sleep(1000);
    				MotionEvent ev = MotionEvent.obtain(downTime, eventTime, 
    						MotionEvent.ACTION_DOWN, 100, 100, 0); 
    						        this.onTouchEvent(ev); 
    			}}}).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	catchTime = System.nanoTime();
    	longList.add(String.format("%ld", catchTime - fireTime));
    	return true;
    }

    @Override
    public void onDestroy() {
    	try {
			FileWriter f = new FileWriter("/sdcard/reslog.txt");
			for (int i = 0; i < longList.size(); i++)
				f.append(longList.get(i));
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}