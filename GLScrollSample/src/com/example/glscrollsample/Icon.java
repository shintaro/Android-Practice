package com.example.glscrollsample;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Icon {
	static final float hen = 38.0f;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private ByteBuffer indexBuffer;

	private float vertices[] = { 
								-hen, -hen, 0.0f,
								hen, -hen, 0.0f,
								-hen, hen, 0.0f,
								hen, hen, 0.0f
	};	

    /** The initial texture coordinates (u, v) */
	private float texture[] = {
									0.0f, 0.0f,
								    1.0f, 0.0f,
								    0.0f, 1.0f,
								    1.0f, 1.0f
	};

    private byte indices[] = {
                                        0,1,3, 0,3,2,                   //Face front
                                        4,5,7, 4,7,6,                   //Face right
                                        8,9,11, 8,11,10,                //... 
                                        12,13,15, 12,15,14,
                                        16,17,19, 16,19,18,
                                        20,21,23, 20,23,22,
};

    private int[] textures = new int[idd.length+1];

	public Icon() {
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
        //
        byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
	}

	public void drawOES(GL10 gl, float x, float y, int tex) {
		
		int[] rect = {0, 0, 64, 64};
		
		x = x * 320.0f / 480.0f;
		y = y * 320.0f / 480.0f;

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[tex]);

        //Point to our buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CW);

		//Enable vertex buffer
//		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glPushMatrix();
		
		//Set The Color To Blue
		gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);	
		
//		gl.glTranslatef(x, y, 0.0f);

		//Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glEnable(GL10.GL_TEXTURE_2D);
//		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		((GL11)gl).glTexParameteriv(GL10.GL_TEXTURE_2D,
				GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
        
		((GL11Ext)gl).glDrawTexfOES(x, y, 0, 64*2/3, 64*2/3 );

		//Draw the vertices as triangle strip
//		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

		gl.glPopMatrix();
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
	}

	public void draw(GL10 gl, float x, float y, int tex) {		
        //Bind our only previously generated texture in this case
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[tex]);

        //Point to our buffers
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CW);

		//Enable vertex buffer
//		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glPushMatrix();
		
		//Set The Color To Blue
		gl.glColor4f(0.5f, 0.5f, 1.0f, 1.0f);	
		
		gl.glTranslatef(x, y, 0.0f);

		//Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		//Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

		gl.glPopMatrix();
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

    public void loadGLTexture(GL10 gl, Context context) {
        //Generate one texture pointer...
    	gl.glGenTextures(idd.length+1, textures, 0);
        
    	for (int i = 0; i < idd.length; i++) {
    		InputStream is = context.getResources().openRawResource(idd[i]);
    		Bitmap bitmap = null;
    		try {
    			bitmap = BitmapFactory.decodeStream(is);

    		} finally {
    			try {
    				is.close();
    				is = null;
    			} catch (IOException e) {
    			}
    		}
	
	        //...and bind it to our array
    		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
	
	        //Create Nearest Filtered Texture
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
	        //Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	
	        //Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
	        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	
	        //Clean up
	        bitmap.recycle();
    	}
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
