package com.bench.memtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Memtest extends Activity {
    private TextView tv1, tv2, tv3;
    private Button button;
    private int size, count;
	private long time;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv1 = (TextView)findViewById(R.id.textView1);
        tv1.setText("Copy Size");

        tv2 = (TextView)findViewById(R.id.textView2);
        tv2.setText("Copy Count");

        tv3 = (TextView)findViewById(R.id.textView3);
        tv3.setText("Result:");
        
        size = 16;
        count = 10000;

        button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				time = memcpytestJNI(size, count);
		        tv3.setText(String.format("Result: %d", time));
			}
		});
        button.setText("START");
    }
    
    private native long memcpytestJNI(int v1, int v2);
    
    static {
    	Log.i("****************", System.getProperty("java.library.path"));
        System.loadLibrary("mem-jni");
    }
}