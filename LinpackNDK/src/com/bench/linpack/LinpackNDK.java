package com.bench.linpack;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LinpackNDK extends Activity {
    private TextView tv;
    private Button b;
    private EditText et;
    private int r;
    private Handler handler;
    private Timer timer;
    private WakeLock wakelock;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main);
        
        wakelock = ((PowerManager)getSystemService(POWER_SERVICE))
        			.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
        			|PowerManager.ACQUIRE_CAUSES_WAKEUP
        			|PowerManager.ON_AFTER_RELEASE, "disableLock");
        
        tv = (TextView)findViewById(R.id.textView1);
        b = (Button)findViewById(R.id.button1);
        et = (EditText)findViewById(R.id.editText1);
        
        b.setText("START");
        handler = new Handler();
        b.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View arg0) {
				final int arsize = Integer.parseInt(et.getText().toString());
				if (arsize >= 10 && arsize <= 4000) {
					tv.setText("Processing.");
					new Thread(new Runnable() {
						public void run() {
				            timer = new Timer(true);
							timer.schedule(new TimerTask() {
								@Override
								public void run() {
								    handler.post(new Runnable() {
								    	public void run() {
								    		tv.append(".");
								    	}
								    });
								}
							}, 1000, 1000);
							wakelock.acquire();
							r = test(arsize);
							wakelock.release();
							timer.cancel();
							timer = null;
						    handler.post(new Runnable() {
						    	public void run() {
						    		printResult();
						    	}
						    });
						}
					}).start();
				}
				else
					tv.setText("Please enter number between 10 and 4000\n");
			}
		});
    }

    private void printResult() {
        if (r != 0) {
        	tv.setText("Required %d Memory.  Aborted.\n");
        	return;
        }
        
        int line = getLineCountJNI();
        tv.setText("");
        for (int i = 0; i < line; i++) {
        	String s = new String(getResultJNI(i));
        	tv.append(s);
        }    	
    }
    
    private int test(int size) {
    	return testJNI(size);
    }

    private native int getLineCountJNI();
    private native String getResultJNI(int i);
    private native int testJNI(int i);
    
    static {
        System.loadLibrary("linpack-jni");
    }
}
