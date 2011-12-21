package com.four_envelope.android;

import android.app.Application;
import android.content.Context;

public class FourEnvelopeApplication extends Application {
	
	private static Context context;

    public void onCreate() {
    	FourEnvelopeApplication.context = getApplicationContext();
    }

	public static Context getContext() {
		return context;
	}
	
}