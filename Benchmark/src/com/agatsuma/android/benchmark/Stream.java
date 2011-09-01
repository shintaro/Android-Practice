package com.agatsuma.android.benchmark;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class Stream extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.stream);
    
        wakelock = ((PowerManager)getSystemService(POWER_SERVICE))
		.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
		|PowerManager.ACQUIRE_CAUSES_WAKEUP
		|PowerManager.ON_AFTER_RELEASE, "disableLock");
        
        tv = (TextView)findViewById(R.id.tv);
        b = (Button)findViewById(R.id.bt);
        
        pd = new ProgressDialog(this);
        handler = new Handler();
        
        b.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View arg0) {
					pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	                pd.setMessage("Benchmark running");
	                pd.setCancelable(true);
	                pd.show();					
					new Thread(new Runnable() {
						public void run() {
							wakelock.acquire();
                            runStream(200000);
							wakelock.release();
						    handler.post(new Runnable() {
						    	public void run() {
						    		pd.dismiss();
						    		printResult();
						    	}
						    });
						}
					}).start();
	
        	}
        });
    	
    }
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);        
        outState.putString("STRING",tv.getText().toString() );
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tv.setText(savedInstanceState.getString("STRING"));        
    }
    
    private void printResult() {
      tv.setText("");
      for (int i = 0; i < 13; i++) {
      	String s = new String(getResult(i));
      	Log.i("**************************", s);
      	tv.append(s);
      }    	
  }

    private native String getResult(int i);
    private native int runStream(int i);
    
    static {
        System.loadLibrary("stream-jni");
    }
    
    private TextView tv;
    private Button b;
    private ProgressDialog pd;
    private WakeLock wakelock;
    private Handler handler;
    private InputMethodManager imm;
}
