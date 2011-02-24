package com.example.longtextviewer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;

import android.util.Log;

public class Thre {
	
		
	public String getRes() {
		String s = "";
		return s;
	}
	
	public void getMessage() {
		FileReader fr;
		CharBuffer cb;
		
		try {
			fr = new FileReader("1298252122.dat");
		} catch (FileNotFoundException e) {
			Log.d("File Handler:","Cannot Open File!");
			System.exit(1);
		}
		
		try {
			fr.read(cb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
