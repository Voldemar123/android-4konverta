package com.four_envelope.android.store;

import com.four_envelope.android.Constants;
import com.four_envelope.android.model.User;
import com.four_envelope.android.rest.RestUser;

/**
 * Work with stored User objects 
 * @author VMaximenko
 *
 */
public class StoreUser extends BaseObjectStore {

	public StoreUser() {
		mRestClient = new RestUser();
		
		mObjectPathName = Constants.APP_CACHE_PATH + StoreClient.getLogin();
		mObjectFileName = mObjectPathName + "/user";
	}
	
	public User getData(boolean update) throws Exception {
		mNeedUpdate = update;
		return (User) processObject();
	}

}
