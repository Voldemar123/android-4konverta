package com.four_envelope.android.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.four_envelope.android.R;
import com.four_envelope.android.operation.LocalizedException;
import com.four_envelope.android.rest.BaseObjectRest;

import android.os.Environment;
import android.util.Log;

/**
 * Base methods for store objects
 * @author VMaximenko
 *
 */
public class BaseObjectStore {

	Boolean mNeedUpdate = false;
	BaseObjectRest mRestClient;

	private Object mStoredObject;
	String mObjectPathName, mObjectFileName;

	private boolean mExternalStorageAvailable, mExternalStorageWriteable;

	
	public Object processObject() throws LocalizedException {
		if ( !mNeedUpdate )
			restoreObject();
		
		if ( mStoredObject == null || mNeedUpdate ) {
			storeObject( mRestClient.retrieve() );
			mNeedUpdate = false;
		}

		return mStoredObject;
	}

	
	/**
	 * Store object to the file system by serialize	
	 */
		protected void storeObject(Object obj) throws LocalizedException {
			checkExternalStorage();
			checkObjectPath();

		    File file = new File(mObjectFileName);
			Log.i(getClass().getSimpleName(), "store " + file.getAbsolutePath());
		    
	        OutputStream os;
	        ObjectOutputStream oos;
	        
			try {
				os = new FileOutputStream(file);
		        oos = new ObjectOutputStream(os);

		        oos.writeObject(obj);

		        oos.close();
		        os.close();
		        
		        mStoredObject = obj;
		        
			} catch (Exception e) {
				Log.e( getClass().getSimpleName(), file.toString(), e );
				throw new LocalizedException( R.string.error_write_storage );
			}
		}

	/** 
	 * Check exist path to stored object	
	 */
		private void checkObjectPath() {
			File file = new File(mObjectPathName);
			file.mkdirs();
		}

	/**
	 * Restore the object from file	
	 */
		private void restoreObject() throws LocalizedException {
			checkExternalStorage();

		    File file = new File(mObjectFileName);
		    Log.i(getClass().getSimpleName(), "restore " + file.getAbsolutePath());
		    
		    if (!file.exists()) {
		    	Log.i(getClass().getSimpleName(), "file not exists");
		    	
		    	mNeedUpdate = true;
		    	return;
		    }
		    
	        InputStream is;
	        ObjectInputStream ois;
		    
	        try {
				is = new FileInputStream(file);
				ois = new ObjectInputStream(is);
				
				mStoredObject = ois.readObject();
				
				ois.close();
				is.close();
				
			} catch (Exception e) {
				Log.e( getClass().getSimpleName(), file.toString(), e );
				throw new LocalizedException( R.string.error_read_storage );
			}
			
		}
	
	protected void checkExternalStorage() throws LocalizedException {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if ( !mExternalStorageAvailable || !mExternalStorageWriteable )
			throw new LocalizedException( R.string.error_external_storage );
	}

	public void refresh() {
		this.mNeedUpdate = true;
	}
	
}
