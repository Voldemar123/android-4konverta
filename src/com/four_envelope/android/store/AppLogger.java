package com.four_envelope.android.store;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.text.TextUtils;
import android.util.Log;

import com.four_envelope.android.Constants;

/**
 * API for sending log output. 
 * Store logs to text file on SD card 
 * @author VMaximenko
 *
 */
public class AppLogger {
	
	private static final String TAG = "4konverta";
	private static String message;
	
	private final static boolean WRITE_LOGS_TO_FILE = true;
	private final static String mLogFileName = TAG + ".log";
	private static RandomAccessFile sLogFile;

	
	public static void appendLog(String text)
	{       
		if (!WRITE_LOGS_TO_FILE)
			return;
		
		try {
			sLogFile = new RandomAccessFile( Constants.APP_CACHE_PATH + mLogFileName, "rwd" );

			sLogFile.seek( sLogFile.length() );
			sLogFile.writeBytes( text + "\n");
			
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			e.printStackTrace();
		}

	}
	
	public static void error(String msg, Throwable tr) {
		if ( Log.isLoggable(TAG, Log.ERROR) ) {
			message = getLocation() + msg;
			
			Log.e(TAG, message, tr);
			appendLog( "[ERROR] " + message + " " + Log.getStackTraceString(tr) );
		}
	}

	public static void warn(String msg) {
		if ( Log.isLoggable(TAG, Log.WARN) ) {
			message = getLocation() + msg;
			
			Log.w(TAG, message);
			appendLog( "[WARN] " + message );
		}
	}

	public static void info(String msg) {
		if ( Log.isLoggable(TAG, Log.INFO) ) {
			message = getLocation() + msg;
			
			Log.i(TAG, message);
			appendLog( "[INFO] " + message );
		}
	}
	
	private static String getLocation() {
		final String className = AppLogger.class.getName();
		final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		boolean found = false;

		for (int i = 0; i < traces.length; i++) {
			StackTraceElement trace = traces[i];

			try {
				if (found) {
					if (!trace.getClassName().startsWith(className)) {
						Class<?> clazz = Class.forName(trace.getClassName());
						return "[" + getClassName(clazz) + ":"
								+ trace.getMethodName() + ":"
								+ trace.getLineNumber() + "]: ";
					}
				} else if (trace.getClassName().startsWith(className)) {
					found = true;
					continue;
				}
			} catch (ClassNotFoundException e) {
			}
		}

		return "[]: ";
	}

	private static String getClassName(Class<?> clazz) {
		if (clazz != null) {
			if (!TextUtils.isEmpty(clazz.getSimpleName())) {
				return clazz.getSimpleName();
			}

			return getClassName(clazz.getEnclosingClass());
		}

		return "";
	}
}
