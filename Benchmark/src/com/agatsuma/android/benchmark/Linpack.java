package com.agatsuma.android.benchmark;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Linpack extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.linpack);
    	
        wakelock = ((PowerManager)getSystemService(POWER_SERVICE))
		.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
		|PowerManager.ACQUIRE_CAUSES_WAKEUP
		|PowerManager.ON_AFTER_RELEASE, "disableLock");
        
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  

        tv = (TextView)findViewById(R.id.textView1);
        b = (Button)findViewById(R.id.button1);
        et = (EditText)findViewById(R.id.editText1);
        
        pd = new ProgressDialog(this);
        handler = new Handler();
        
        resultStringList = new ArrayList<String>();
        
        b.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View arg0) {
				arraySize = Integer.parseInt(et.getText().toString());
				if (arraySize >= 10 && arraySize <= 4000) {
				    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);  
					pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	                pd.setMessage("Benchmark running");
	                pd.setCancelable(true);
	                pd.show();					
					new Thread(new Runnable() {
						public void run() {
							wakelock.acquire();
							runLinpack(arraySize);
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
				else
					tv.setText("Please enter number between 10 <-> 4000\n");        		
        	}
        });
    }
    
    private void printResult() {
    	tv.setText(String.format("\nLINPACK benchmark, %s precision.\n", getPrecision()));
    	tv.append(String.format("Machine precision:  %d digits.\n", getMachinePrecision()));
    	tv.append(String.format("Array size %d X %d.\n", arraySize, arraySize));
    	tv.append("Average rolled and unrolled performance:\n\n");
    	tv.append("    Reps Time(s) DGEFA   DGESL  OVERHEAD    KFLOPS\n");
    	tv.append("----------------------------------------------------\n");
    	Iterator<String> iter = resultStringList.iterator();
        while(iter.hasNext()) {
        	tv.append(iter.next());
        }
    }
 	
    private void setResultStringFromNative(String s) {
    	resultStringList.add(s);
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
    
    private native String getPrecision();
    private native int getMachinePrecision();
    private native int runLinpack(int i);
    
    static {
        System.loadLibrary("linpack-jni");
    }
    
    private TextView tv;
    private Button b;
    private EditText et;
    private ProgressDialog pd;
    private WakeLock wakelock;
    private Handler handler;
    private InputMethodManager imm;
    
    private int arraySize;
    private ArrayList<String> resultStringList;
}
