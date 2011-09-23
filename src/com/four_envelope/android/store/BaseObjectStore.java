package com.four_envelope.android.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;

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

	
	public Object processObject() throws Exception {
		Log.i(getClass().getSimpleName(), "processObject");

//		mStoredObject = storedObject;

//		if (mStoredObject != null && !mNeedUpdate)
//			return mStoredObject;

//		if (mStoredObject == null)
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
	 * @param obj
	 */
		protected synchronized void storeObject(Object obj) {
			Log.i(getClass().getSimpleName(), "storeObject");
			
			checkExternalStorage();
			checkObjectPath();

		    File file = new File(mObjectFileName);
		    
	        OutputStream os;
	        ObjectOutputStream oos;
	        
			try {
				os = new FileOutputStream(file);
		        oos = new ObjectOutputStream(os);

		        oos.writeObject(obj);

		        oos.close();
		        os.close();
		        
		        mStoredObject = obj;
		        
			} catch (FileNotFoundException e) {
				Log.e( getClass().getSimpleName(), "FileNotFound " + file, e );
				e.printStackTrace();
			} catch (IOException e) {
				Log.e( getClass().getSimpleName(), "Error writing " + file, e );
				e.printStackTrace();
			}
		}

	/** 
	 * Check exist path to stored object	
	 */
		private void checkObjectPath() {
			Log.i(getClass().getSimpleName(), "checkObjectPath " + mObjectPathName);
			
			File file = new File(mObjectPathName);
			file.mkdirs();
		}

	/**
	 * Restore the object from file	
	 */
		private void restoreObject() {
			Log.i(getClass().getSimpleName(), "restoreObject");
			
			checkExternalStorage();

		    File file = new File(mObjectFileName);
		    Log.i(getClass().getSimpleName(), "from "+file.getAbsolutePath());
		    
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
				
			} catch (FileNotFoundException e) {
				Log.e( getClass().getSimpleName(), "FileNotFound " + file, e );
			} catch (StreamCorruptedException e) {
				Log.e( getClass().getSimpleName(), "StreamCorrupted " + file, e );
			} catch (IOException e) {
				Log.e( getClass().getSimpleName(), "Error reading " + file, e );
			} catch (ClassNotFoundException e) {
				Log.e( getClass().getSimpleName(), "ClassNotFound " + file, e );
			}
			
		}
	
	protected void checkExternalStorage() {
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

		// TODO throw error
		
	}

	public void refresh() {
		this.mNeedUpdate = true;
	}

	
	
}
