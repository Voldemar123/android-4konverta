package com.four_envelope.android.store;

import android.content.SharedPreferences;

/**
 * Application client authorization
 * @author VMaximenko
 *
 */
public class StoreClient {

	private static SharedPreferences settings;
	private static SharedPreferences.Editor editor;
	
	public static void setPreferences(SharedPreferences appSettings) {
		settings = appSettings;
		editor = settings.edit();
	}

	public static String getLogin() {
		return settings.getString("login", null);
	}

	public static void setLogin(String login) {
		editor.putString("login", login);
	    editor.commit();
	}

	public static String getPassword() {
		return settings.getString("password", null);
	}

	public static void setPassword(String password) {
		editor.putString("password", password);
	    editor.commit();
	}

	public static void logout() {
		editor.putString("login", null);
		editor.putString("password", null);
	    editor.commit();
	}

	public static boolean isLogged() {
		return ( getLogin() != null && getPassword() != null);
	}
	
	public static String getPersonImage(final Integer personId) {
		return settings.getString("personImage_" + personId, null);
	}

	public static void setPersonImage(final Integer personId, final String personImage) {
		editor.putString("personImage_" + personId, personImage);
	    editor.commit();
	}
	
}
