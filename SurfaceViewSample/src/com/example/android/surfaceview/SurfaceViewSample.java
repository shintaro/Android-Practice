package com.example.android.surfaceview;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.MenuItem.OnMenuItemClickListener;

public class SurfaceViewSample extends Activity {
	private MySurfaceView mSurfaceView;
	private int first;
	private int y =0;
	private long timerStart = 0;
	private boolean isTraced = false;
	private boolean isTracing = false;
	private boolean isTraceOut = false;
	
	private Timer mTimer;
	private Handler mHandler;
   
	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

		private Bitmap[] bmp;
		protected SurfaceHolder mHolder;
		private Resources r;
		public int ypos;
		private boolean drawByEvent = true;

		public MySurfaceView(Context context) {
			super(context);
			bmp = new Bitmap[100];
			getHolder().addCallback(this);
			r = getResources();
		}

		//@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			Log.d("***************", "surfaceChanged");
		}

		//@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d("***************", "surfaceCreated");
			mHolder = holder;
			
			for (int i = 0; i < 24; i++) {
				for (int j = 0; j < 4; j++) {
					bmp[i*4+j] = BitmapFactory.decodeResource(r, idd[i*4+j]);
	        		bmp[i*4+j] = Bitmap.createScaledBitmap(bmp[i*4+j], 64, 64, false);
	        	}
	        }
			
			if (!mSurfaceView.drawByEvent) {
				startTimerEvent();
			}
			else { 			
				draw2();
			}
		}

		public void draw(SurfaceHolder holder) {
		}		
		
		public void draw2() {
			Canvas canvas = mHolder.lockCanvas();
			Paint paint = new Paint();
			canvas.drawColor(Color.WHITE);

			for (int i = 0; i < 24; i++) {
				for (int j = 0; j < 4; j++) {
					canvas.drawBitmap(bmp[i*4+j], 32+(j*120), 120*(i+1)+ypos, paint);
	        	}
	        }
	        
			mHolder.unlockCanvasAndPost(canvas);			
		}
		
		//@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.d("*****************", "surfaceDestroyed");
		}
	}		
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	mSurfaceView = new MySurfaceView(this);
    	super.onCreate(savedInstanceState);
    	setContentView(mSurfaceView);
    }

    @Override
    public void onDestroy() {
		super.onDestroy();
		mSurfaceView = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	trace();
    	
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		first = (int)event.getY();
    	}
    	
    	if (event.getAction() == MotionEvent.ACTION_MOVE) {
    		mSurfaceView.ypos = y + (int)event.getY() - first;
    		if (mSurfaceView.drawByEvent)
    			mSurfaceView.draw2();
    	}
    	
    	if (event.getAction() == MotionEvent.ACTION_UP) {
    		y = mSurfaceView.ypos;
    	}
       
       return true;
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item1, item2;
		item1 = menu.add("Timer Draw Mode");
		item1.setCheckable(true);
		item2 = menu.add("Event Draw Mode");
		item2.setCheckable(true);
		if (mSurfaceView.drawByEvent) {
			item1.setChecked(true);
			item2.setChecked(false);	
		} else {
			item1.setChecked(false);
			item2.setChecked(true);				
		}
			
		item1.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		    public boolean onMenuItemClick(MenuItem item) {
		    	if (mSurfaceView.drawByEvent) {
		    		mSurfaceView.drawByEvent = !mSurfaceView.drawByEvent;
		    		timerStart = 0;
		    		isTraced = false;
		    		isTracing = false;
		    		startTimerEvent();
		    	}
				return false;
		    }
		});

		item2.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		    public boolean onMenuItemClick(MenuItem item) {
		    	if (!mSurfaceView.drawByEvent) {
		    		mSurfaceView.drawByEvent = !mSurfaceView.drawByEvent;
		    		timerStart = 0;
		    		isTraced = false;
		    		isTracing = false;
		    		stopTimerEvent();
				}
				return false;
		    }
		});
		
		return true;
	}

	private void trace() {
		if (isTraceOut) {
	    	if (isTraced == false) {
	    		if (timerStart == 0) {
	    			timerStart = System.currentTimeMillis();
	    		} 
	
	    		if (System.currentTimeMillis() - 1000 > timerStart && isTracing == false) {
	    			timerStart = System.currentTimeMillis();
	    			isTracing = true;
	    			Debug.startMethodTracing("SurfaceViewSample_Event");
	    		}
	    		
	    		if (System.currentTimeMillis() - 1000 > timerStart && isTracing == true) {
	    				Debug.stopMethodTracing();
	    				isTracing = false;
	    				isTraced = true;
	    		}
	    	}
		}
	}
	
	private void startTimerEvent() {
		mTimer = new Timer(true);
		mHandler = new Handler();
		mTimer.schedule(new TimerTask() {
			public void run() {
				mHandler.post(new Runnable() {
					public void run() {
						trace();
				    	mSurfaceView.draw2();
						}
					});
				}
			}, 17, 17);
	}

	private void stopTimerEvent() {
		mTimer.cancel();
		mTimer = null;
		mHandler = null;
	}

    static final int[] idd = {
		R.drawable.aim,
		R.drawable.amazon1,
		R.drawable.amazon2,
		R.drawable.android,
		R.drawable.aol,
		R.drawable.apple,
		R.drawable.appstore,
		R.drawable.bebo,
		R.drawable.behance,
		R.drawable.bing,
		R.drawable.bleetbox,
		R.drawable.blinklist,
		R.drawable.blogger,
		R.drawable.brightkite1,
		R.drawable.brightkite2,
		R.drawable.cargocollective,
		R.drawable.coroflot,
		R.drawable.delicious,
		R.drawable.designfloat,
		R.drawable.designmoo,
		R.drawable.deviantart,
		R.drawable.digg,
		R.drawable.diglog,
		R.drawable.doppler,
		R.drawable.dribbble,
		R.drawable.dzone,
		R.drawable.ebay,
		R.drawable.ember,
		R.drawable.evernote,
		R.drawable.facebook,
		R.drawable.feedburner1,
		R.drawable.feedburner2,
		R.drawable.flickr1,
		R.drawable.flickr2,
		R.drawable.foursquare,
		R.drawable.fresqui,
		R.drawable.friendfeed,
		R.drawable.friendster,
		R.drawable.furl,
		R.drawable.gamespot,
		R.drawable.gmail,
		R.drawable.googlebuzz,
		R.drawable.google,
		R.drawable.gowalla,
		R.drawable.gravee,
		R.drawable.grooveshark1,
		R.drawable.grooveshark2,
		R.drawable.gtalk,
		R.drawable.hi51,
		R.drawable.hi52,
		R.drawable.hyves1,
		R.drawable.hyves2,
		R.drawable.identica,
		R.drawable.ilike,
		R.drawable.isociety,
		R.drawable.lastfm,
		R.drawable.linkedin,
		R.drawable.livejournal,
		R.drawable.magnolia,
		R.drawable.metacafe,
		R.drawable.misterwong,
		R.drawable.mixx,
		R.drawable.mobileme,
		R.drawable.msn1,
		R.drawable.msn2,
		R.drawable.mynameise,
		R.drawable.myspace,
		R.drawable.netvibes,
		R.drawable.newsvine,
		R.drawable.ning,
		R.drawable.openid1,
		R.drawable.openid2,
		R.drawable.orkut,
		R.drawable.pandora,
		R.drawable.paypal,
		R.drawable.picasa,
		R.drawable.pimpthisblog,
		R.drawable.plurk,
		R.drawable.posterous,
		R.drawable.qik,
		R.drawable.readernaut,
		R.drawable.reddit,
		R.drawable.rss,
		R.drawable.sharethis,
		R.drawable.skype,
		R.drawable.slashdot,
		R.drawable.sphere,
		R.drawable.sphinn,
		R.drawable.spotify,
		R.drawable.springpad,
		R.drawable.stumbleupon,
		R.drawable.technorati,
		R.drawable.tripadvisor,
		R.drawable.tuenti,
		R.drawable.tumblr,
		R.drawable.twitter,
		R.drawable.viddler,
		R.drawable.vimeo,
		R.drawable.virb,
		R.drawable.webshots,
		R.drawable.windows,
		R.drawable.wordpress,
		R.drawable.xing,
		R.drawable.yahoobuzz,
		R.drawable.yahoo,
		R.drawable.yelp,
		R.drawable.youtube,
		R.drawable.zanatic,
		R.drawable.zootool
    };
}
