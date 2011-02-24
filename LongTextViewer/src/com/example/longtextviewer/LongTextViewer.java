package com.example.longtextviewer;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class LongTextViewer extends Activity {
    /** Called when the activity is first created. */
	private ListView listview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listview = (ListView)findViewById(R.layout.m);
        
        
    }
}

