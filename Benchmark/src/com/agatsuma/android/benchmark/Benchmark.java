package com.agatsuma.android.benchmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Benchmark extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        rg = (RadioGroup)findViewById(R.id.radioGroup1);
        bt = (Button)findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            //@Override  
            public void onClick(View v) {  
            	Intent intent;
            	switch (rg.getCheckedRadioButtonId()) {
            	case R.id.rbLinpack:
                	intent = new Intent(Benchmark.this, Linpack.class);
            		break;
            	case R.id.rbStream:
                	intent = new Intent(Benchmark.this, Stream.class);
            		break;
            	case R.id.rbLatency:
                	intent = new Intent(Benchmark.this, Latency.class);
            		break;
            	default:
            		return;
            	}            	
            	startActivity(intent);
            }                
        });  
    }
    
    private Button bt;
    private RadioGroup rg;
}