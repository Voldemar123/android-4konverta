package com.four_envelope.android.store;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import com.four_envelope.android.R;
import com.four_envelope.android.operation.LocalizedException;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

/**
 * Person bitmap works
 * Select image from gallery, scale, load from cache 
 * @author VMaximenko
 *
 */
public class PersonImage extends BaseStore {

	private static ContentResolver mContentResolver;

	protected static Bitmap placeholder;
	private static final String TEMP_PHOTO_FILE = "_person.png";
	
	private static BaseStore store;
	
	private static Map<Integer, SoftReference<Bitmap>> cache;
	

	public static void setResources(Context context) {
		mContentResolver = context.getContentResolver();
		placeholder = BitmapFactory.decodeResource( context.getResources(), R.drawable.icon );

		cache = new HashMap<Integer, SoftReference<Bitmap>>();
		store = new BaseStore();
	}

	public static void load(final ImageView imageView, final Integer personId) {
		Bitmap bitmap = getPersonImageFromCache(personId);

		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else
			loadBitmap(imageView, personId);
	}

	public static void save(final Integer personId, final Uri selectedImageUri) {
		StoreClient.setPersonImage(personId, selectedImageUri.toString());
	}

	public static Bitmap getPersonImageFromCache(final Integer personId) {
		if (cache.containsKey(personId)) {
			return cache.get(personId).get();
		}

		return null;
	}

	private static void loadBitmap(final ImageView imageView, final Integer personId) {
		String personImage = StoreClient.getPersonImage(personId);
		
		Bitmap bitmap;
		if (personImage == null)
			bitmap = placeholder;
		else {
			bitmap = downloadBitmap(personImage);
			cache.put( personId, new SoftReference<Bitmap>(bitmap) );
		}
		
		imageView.setImageBitmap(bitmap);
	}

	private static Bitmap downloadBitmap(String url) {
		try {
			return BitmapFactory.decodeStream(
					mContentResolver.openInputStream( 
							Uri.parse( url ) ) );
			
		} catch (IOException e) {
			Log.e( "PersonImage", e.getMessage() );
		}

		return null;
	}
	
	public static Uri getTempUri(final Integer personId) {
        return Uri.fromFile( getTempFile(personId) );
    }
	
	public static File getTempFile(final Integer personId) {
		try {
			store.checkExternalStorage();
			store.checkObjectPath();

			File f = new File( store.mObjectPathName, personId + TEMP_PHOTO_FILE);
	        f.createNewFile();
	        
	        return f;
	        
		} catch (LocalizedException e) {
			Log.e( "PersonImage", e.getMessage() );
		} catch (IOException e) {
			Log.e( "PersonImage", e.getMessage() );
		}

        return null;
    }
}
