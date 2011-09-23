package com.four_envelope.android;

import android.os.Environment;

public class Constants {

	public static final String APP_PREFS_NAME = "com.four_envelope.android";
	public static final String APP_CACHE_PATH = 
		Environment.getExternalStorageDirectory().getAbsolutePath() + 
		"/Android/data/" + APP_PREFS_NAME + "/cache/";
	
	public static final String REST_APP_NAME = "Demo";
	public static final String REST_API_VERSION = "1.2";
	public static final String REST_TARGET_DOMAIN = "www.4konverta.com";
	

}
