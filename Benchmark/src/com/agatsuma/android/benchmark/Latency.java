package com.agatsuma.android.benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Latency extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.latency);
    
        wakelock = ((PowerManager)getSystemService(POWER_SERVICE))
        .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
        |PowerManager.ACQUIRE_CAUSES_WAKEUP
        |PowerManager.ON_AFTER_RELEASE, "disableLock");
        
        bAutoTest = (Button)findViewById(R.id.bAutoTest);
        bStart = (Button)findViewById(R.id.bStart);
        etMemSize = (EditText)findViewById(R.id.etMemSize);
        etStride = (EditText)findViewById(R.id.etStride);
        tvResult = (TextView)findViewById(R.id.tvResult);


        handler = new Handler();

        bStart.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View arg0) {
        		if (!isTestRun) {
        			isTestRun = true;
                    tvResult.setText("Auto Test running.");
                    new Thread(new Runnable() {
                    	public void run() {
                    		timer = new Timer(true);
                            timer.schedule(new TimerTask() {
                            	@Override
                                public void run() {
                            		handler.post(new Runnable() {
                            			public void run() {
                            				tvResult.setText(sb);
                                        }
                                    });
                                }
                            }, 1000, 1000);
                            wakelock.acquire();
                            singleTest();
                            wakelock.release();
                            timer.cancel();
                            timer = null;
                            handler.post(new Runnable() {
                            	public void run() {
                            		tvResult.setText(sb);
                                }
                            });
                        }
                    }).start();
                }
                isTestRun = false;
            }
        });

        bAutoTest.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                if (!isTestRun) {
                	isTestRun = true;
                    tvResult.setText("Auto Test running.");
                    new Thread(new Runnable() {
                    	public void run() {
                    		timer = new Timer(true);
                            timer.schedule(new TimerTask() {
                            	@Override
                            	public void run() {
                            		handler.post(new Runnable() {
                            			public void run() {
                            				tvResult.append(".");
                                        }
                                    });
                                }
                            }, 1000, 1000);
                            wakelock.acquire();
                            isTestSucceed = autoTest();
                            wakelock.release();
                            timer.cancel();
                            timer = null;
                            handler.post(new Runnable() {
                            	public void run() {
                            		if (isTestSucceed)
                            			tvResult.setText(String.format("Test Result is saved to %s\n", fileName));
                                    else
                                        tvResult.setText("Test is failed.  Please check your SD\n");
                                }
                            });
                        }
                    }).start();
                }
                isTestRun = false;
            }
        });
}

    private void singleTest() {
    	memSize = Integer.parseInt(etMemSize.getText().toString());
        stride = Integer.parseInt(etStride.getText().toString());
        len = memSize*1024*1024;
        sb = new StringBuffer(String.format("Stride = %d\n", stride));
        InputMethodManager inputMethodManager =
        	(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etMemSize.getWindowToken(), 0);
        for (range = LOWER; range <= len; range = step(range)) {
        	result = loads(len, range, stride, parallel, warmup, repetitions);
            sb.append(String.format("%.5f %.3f\n", (float)range / (1024*1024), result));
        }
    }
    
	private boolean autoTest() {
		FileWriter fw;
		Calendar cal;

		memSize = 32;
		len = memSize*1024*1024;
		InputMethodManager inputMethodManager =
			(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(etMemSize.getWindowToken(), 0);

		cal = Calendar.getInstance();
		fileName = String.format("/sdcard/LMBenchResult%d%d%d%d%d%d.txt",
                                                    cal.get(Calendar.YEAR),
                                                    cal.get(Calendar.MONTH),
                                                    cal.get(Calendar.DAY_OF_MONTH),
                                                    cal.get(Calendar.HOUR_OF_DAY),
                                                    cal.get(Calendar.MINUTE),
                                                    cal.get(Calendar.SECOND));

		try {
			fw = new FileWriter(fileName);
		} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
		}

		for (int i = 0; i < strideAutoTest.length; i++) {
            stride = strideAutoTest[i];
            try {
            	fw.write(String.format("Stride = %d\n", stride));
            } catch (IOException e1) {
            	// TODO Auto-generated catch block
                e1.printStackTrace();
                return false;
            }
            for (range = LOWER; range <= len; range = step(range)) {
            	result = loads(len, range, stride, parallel, warmup, repetitions);
                Log.i("*********", String.format("%.5f %.3f\n", (float)range / (1024*1024), result));
                try {
                	fw.write((String.format("%.5f %.3f\n", (float)range / (1024*1024), result)));
                } catch (IOException e) {
                	// TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
		}

		try {
            fw.close();
		} catch (IOException e) {
            return false;
		}

		return true;
	}

	private int step(int k) {
		if (k < 1024) {
			k = k * 2;
		} else if (k < 4*1024) {
			k += 1024;
		} else {
			int s;
			for (s = 4 * 1024; s <= k; s *= 2)
	            ;
			k += s / 4;
		}
		return (k);
	}


	private native double loads(long len, int range, int stride,
        int parallel, int warmup, int reperitions);

	static {
		System.loadLibrary("latency-jni");
	}

	private final int parallel = 1;
	private final int warmup = 0;
	private final int repetitions = -1;
	private final int LOWER = 512;

    private double result;
    private long len;
    private int range, memSize, stride;
    private final int[] strideAutoTest = {16, 32, 64, 128, 256};
    
    private Button bAutoTest, bStart;
    private EditText etMemSize, etStride;
    private TextView tvResult;
    
    private Handler handler;
    private Timer timer;
    private WakeLock wakelock;

    private String fileName;
    private boolean isTestRun = false;
    private boolean isTestSucceed = false;
    private StringBuffer sb;
}
