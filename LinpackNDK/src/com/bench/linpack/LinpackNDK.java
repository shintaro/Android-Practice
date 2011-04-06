package com.bench.linpack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LinpackNDK extends Activity {
    TextView tv;
    Button b;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv = (TextView)findViewById(R.id.textView1);
        b = (Button)findViewById(R.id.button1);
        
        b.setText("START");
        b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				test();
			}
		});

        test();
    }

    private void test() {
        tv.setText("************************\n");        
        testJNI();
        
        for (int i = 0; i < 19; i++) {
        	String s = new String(getResultJNI(i));
        	Log.i("", s);
        	tv.append(s);
        }        	
    }

    private native String getResultJNI(int i);
    private native int testJNI();
    
    static {
        System.loadLibrary("linpack-jni");
    }
}
