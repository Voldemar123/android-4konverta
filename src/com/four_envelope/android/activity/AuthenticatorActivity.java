package com.four_envelope.android.activity;

import com.four_envelope.android.R;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.model.User;
import com.four_envelope.android.store.StoreClient;
import com.four_envelope.android.store.StoreUser;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays login screen to the user.
 */
public class AuthenticatorActivity extends Activity {
    /** Was the original caller asking for an entirely new account? */
    protected boolean mRequestNewAccount = false;
    
    private TextView mMessage;

    private String mPassword;
    private EditText mPasswordEdit;

    private String mUsername;
    private EditText mUsernameEdit;

    @Override
    public void onCreate(Bundle icicle) {        
        super.onCreate(icicle);

        mUsername = StoreClient.getLogin();
        mRequestNewAccount = (mUsername == null);
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.login_activity);
        
        mUsernameEdit = (EditText) findViewById(R.id.username_edit);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit);

//        mUsernameEdit.setText(mUsername);
//        
//        mPasswordEdit.setText(StoreClient.getPassword());

        mMessage = (TextView) findViewById(R.id.message);
        mMessage.setText(getMessage());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final ProgressDialog dialog = new ProgressDialog(this);
        
        dialog.setMessage(getText(R.string.ui_activity_authenticating));
        dialog.setIndeterminate(true);
//        dialog.setCancelable(true);
        
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            public void onCancel(DialogInterface dialog) {
//                Log.i(getClass().getSimpleName(), "dialog cancel has been invoked");
//
//            }
//        });
        
        return dialog;
    }

    /**
     * Handles onClick event on the Submit button. Sends username/password to
     * the server for authentication.
     * 
     * @param view The Submit button for which this method is invoked
     */
    public void handleLogin(View view) {
        mUsername = mUsernameEdit.getText().toString();
        mPassword = mPasswordEdit.getText().toString();

        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
            mMessage.setText(getMessage());
        } else {
            showProgress();

    		StoreClient.setLogin(mUsername);
    		StoreClient.setPassword(mPassword);

            // Start authenticating...
            new GetInfo().execute();
        }
    }

    /**
     * Called when the authentication process completes.
     */
    public void onAuthenticationResult(boolean result) {
        // Hide the progress dialog
        hideProgress();

        if (result)
        	finishLogin();
        else {

        	if (mRequestNewAccount)
                mMessage.setText(getText(R.string.login_fail_text_both));
            else
                mMessage.setText(getText(R.string.login_fail_text_password_only));
        }
    }

    /**
     * 
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. Also sets
     * the authToken in AccountManager for this account.
     * 
     * @param the confirmCredentials result.
     */

    protected void finishLogin() {
        final Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Returns the message to be displayed at the top of the login dialog box.
     */
    private CharSequence getMessage() {
        if (TextUtils.isEmpty(mUsername)) {
            // If no username, then we ask the user to log in using an appropriate service.
        	return getText(R.string.new_account_text);
        }
        if (TextUtils.isEmpty(mPassword)) {
            // We have an account but no password
            return getText(R.string.login_fail_text_password_missing);
        }
        return null;
    }

    /**
     * Shows the progress UI for a lengthy operation.
     */
    protected void showProgress() {
        showDialog(0);
    }
    
    /**
     * Hides the progress UI for a lengthy operation.
     */
    protected void hideProgress() {
        dismissDialog(0);
    }
    
    
	private class GetInfo extends AsyncTask<String, Void, User> {

		@Override
		protected User doInBackground(String... params) {

			try {
				User userData = new StoreUser().getData(true);
				BudgetWork.userData = userData;
				
				return userData;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		protected void onPostExecute(User user) {
			onAuthenticationResult(user != null);
		}

	}

}