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
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewSample extends Activity {
    /** Called when the activity is first created. */
	private MySurfaceView mSurfaceView;
	private int first;
	final private boolean useTimer = false;
    private int y;
    
	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

		private Bitmap[] bmp;
		private Timer mTimer;
		private Handler mHandler = new Handler();
		private SurfaceHolder mHolder;
       private Resources r;
       public int ypos;
//		private Canvas canvas;

		public MySurfaceView(Context context) {
			super(context);
			bmp = new Bitmap[100];
			mTimer = new Timer(true);
			getHolder().addCallback(this);
			r = getResources();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			Log.d("TEST", "surfaceChanged");
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d("TEST", "surfaceCreated");
			mHolder = holder;
			
	        for (int i = 0; i < 24; i++) {
	        	for (int j = 0; j < 4; j++) {
	        		bmp[i*4+j] = BitmapFactory.decodeResource(r, idd[i*4+j]);
	        		bmp[i*4+j] = Bitmap.createScaledBitmap(bmp[i*4+j], 64, 64, false);
	        	}
	        }
	        
	        if (useTimer) {
				mTimer.schedule(new TimerTask() {
					public void run() {
						mHandler.post(new Runnable() {
							public void run() {
								draw(mHolder);
							}
						});
					}
				}, 17, 17);
	        } else {
	        	new Thread(new Runnable() {
	        		public void run() {
	        			while (true)
	        				draw(mHolder);
	        		}}).start();
	        	}
		}

		public void draw(SurfaceHolder holder) {
			Canvas canvas = holder.lockCanvas();
			Paint paint = new Paint();
			canvas.drawColor(Color.WHITE);
//			paint.setColor(Color.BLUE);
//			paint.setAntiAlias(true);
//			paint.setTextSize(24);
//			canvas.drawText("Hello, SurfaceView!", 0, paint.getTextSize(), paint);

	        for (int i = 0; i < 24; i++) {
	        	for (int j = 0; j < 4; j++) {
	        		canvas.drawBitmap(bmp[i*4+j], 32+(j*120), 120*(i+1)+ypos, paint);
	        	}
	        }
	        
			holder.unlockCanvasAndPost(canvas);			
		}
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.d("TEST", "surfaceDestroyed");
			//mSurfaceView = null;
			bmp = null;
			mTimer = null;
			mHandler = null;
			mHolder = null;
			r = null;
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	mSurfaceView = new MySurfaceView(this);
       super.onCreate(savedInstanceState);
       setContentView(mSurfaceView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_DOWN)
    		first = (int)event.getY();
	        
    	if (event.getAction() == MotionEvent.ACTION_MOVE)
    		mSurfaceView.ypos = y + (int)event.getY() - first;
    	Log.v("Move Amount", Integer.toString(mSurfaceView.ypos));

       if (event.getAction() == MotionEvent.ACTION_UP)
       	y = mSurfaceView.ypos;
    	
        return true;
    }
    
    @Override
    public void onDestroy() {
    	//mSurfaceView = null;
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
