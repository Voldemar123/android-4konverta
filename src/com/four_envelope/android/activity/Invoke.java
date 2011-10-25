package com.four_envelope.android.activity;

import com.four_envelope.android.store.PersonImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

public final class Invoke {

	public static class Extras {

		public static final int AUTH_REQUEST = 0;
		public static final int EXECUTION_POPUP_EDITOR = 1;
		public static final int SELECT_PICTURE = 2;
		
		public static final String PERSON_ID = "person_id";

		public static final String EXECUTION_REFRESH = "execution_refresh";
		public static final String EXECUTION_PERSON_ID = "execution_person_id";
		public static final String EXECUTION_PERSON_NAME = "execution_person_name";
		public static final String EXECUTION_DATE = "execution_date";

		public static final String PERSON_DAILY_EXPENSE_RESULT = "person_daily_expense_result";
		
	}

	public static class User {

		public static void authenticate(Activity parent) {
			parent.startActivityForResult(
					new Intent(parent, AuthenticatorActivity.class), 
					Extras.AUTH_REQUEST);
		}

		public static void status(Activity parent) {
			parent.startActivity(
					new Intent( parent, StatusActivity.class ) );
		}

		public static void executionPopupEditor(Activity parent, Boolean refreshContent, Integer personId, String personName, String date) {
			parent.startActivityForResult(
					new Intent(parent, ExecutionPopupEditorActivity.class)
						.putExtra( Extras.EXECUTION_REFRESH, refreshContent)
						.putExtra( Extras.EXECUTION_PERSON_ID, personId)
						.putExtra( Extras.EXECUTION_PERSON_NAME, personName)
						.putExtra( Extras.EXECUTION_DATE, date), 
					Extras.EXECUTION_POPUP_EDITOR);
		}

		public static void selectPersonImage(Activity parent, Integer personId) {
			parent.startActivityForResult(
					new Intent(Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
						.putExtra( Extras.PERSON_ID, personId)
						.putExtra("crop", "true")
						.putExtra("aspectX", 1)
			            .putExtra("aspectY", 1)
			            .putExtra("outputX", 72)
			            .putExtra("outputY", 72)
			            .putExtra("scale", true)
			            .putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
						.putExtra(MediaStore.EXTRA_OUTPUT, PersonImage.getTempUri(personId) )
			            .putExtra("noFaceDetection", false),
					Extras.SELECT_PICTURE);
		}
		
	}
}
