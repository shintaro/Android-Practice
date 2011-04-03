package com.example.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

public class SampleTextViewer extends Activity {

	private static final String TAG = "SampleTextViewer";
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	char[] s = new char[10000];
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	TextView tv = (TextView)this.findViewById(R.id.textView1);

    	FileReader fr;        
    	try {
    		fr = new FileReader("/sdcard/subject.txt");
    		try {
    			fr.read(s);
			} catch (IOException e) {
				Log.e(TAG, "Can NOT read\n");
				System.exit(1);
			}
			try {
				fr.close();
			} catch (IOException e) {

			}
    	} catch (FileNotFoundException e) {
			Log.e(TAG, "File NOT Found\n");
			System.exit(1);
    	}

    	tv.setText(s, 0, 10000);    	
    }
}