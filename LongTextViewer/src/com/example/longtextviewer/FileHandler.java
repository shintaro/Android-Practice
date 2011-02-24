package com.example.longtextviewer;

import java.io.FileNotFoundException;
import java.io.FileReader;

import android.util.Log;

public class FileHandler {
	FileReader fr;

	public FileHandler() {
		try {
			fr = new FileReader("1298252122.dat");
		} catch (FileNotFoundException e) {
			Log.d("File Handler:","Cannot Open File!");
			System.exit(1);
		}
	}
	
	public FileHandler(String filename) {
		try {
			fr = new FileReader(filename);
		} catch (FileNotFoundException e) {
			Log.d("File Handler:","Cannot Open File!");
			System.exit(1);
		}
	}
		
	public String getRes() {
		String s = "";
		return s;
	}
	
}
