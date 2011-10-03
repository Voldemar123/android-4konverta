package com.four_envelope.android.operation;

import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.model.User;
import com.four_envelope.android.store.StoreClient;
import com.four_envelope.android.store.StoreUser;

public class AuthorizeOperation extends AbstractOperation<String, User> {

	public Boolean isSuccessLogin; 
	private String mUsername, mPassword;
	
	public AuthorizeOperation(UpdateListener context) {
		super(context);
	}

	@Override
	User process(String... params) throws LocalizedException {
		mUsername = params[0];
		mPassword = params[1];
		
		StoreClient.setLogin(mUsername);
		StoreClient.setPassword(mPassword);
		
		return new StoreUser().getData(true);
	}

	@Override
	void onSuccess(User result) {
		isSuccessLogin = result != null;
		BudgetWork.userData = result;
		
		if (!isSuccessLogin)
			StoreClient.logout();
	}

	@Override
	void onClear() {
		isSuccessLogin = false;
	}

}
