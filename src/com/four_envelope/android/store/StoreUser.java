package com.four_envelope.android.store;

import com.four_envelope.android.model.User;
import com.four_envelope.android.operation.LocalizedException;
import com.four_envelope.android.rest.RestUser;

/**
 * Work with stored User objects 
 * @author VMaximenko
 *
 */
public class StoreUser extends BaseObjectStore {

	public StoreUser() throws LocalizedException {
		mRestClient = new RestUser();
		
		mObjectFileName = mObjectPathName + "/user";
	}
	
	public User getData(boolean update) throws LocalizedException {
		mNeedUpdate = update;
		return (User) processObject();
	}

}
