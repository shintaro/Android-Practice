package com.bench.stream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StreamBench extends Activity {
    private TextView tv;
    private Button b;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv = (TextView)findViewById(R.id.textView1);
        b = (Button)findViewById(R.id.button1);
        tv.setText("Press START to get benchmark\n");
        b.setText("START");
        b.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View arg0) {
        		testJNI(200000);
        		printResult();
        	}
        });
    }
    
    private void printResult() {
//        int line = getLineCountJNI();
        tv.setText("");
        for (int i = 0; i < 13; i++) {
        	String s = new String(getResultJNI(i));
        	tv.append(s);
        }    	
    }
    
//    private native int getLineCountJNI();
    private native String getResultJNI(int i);
    private native int testJNI(int i);
    
    static {
        System.loadLibrary("stream-jni");
    }
}