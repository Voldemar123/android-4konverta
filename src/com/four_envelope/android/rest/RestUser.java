package com.four_envelope.android.rest;

import com.four_envelope.android.model.User;
import com.four_envelope.android.store.StoreClient;

/**
 * Work with user information on server
 * 
 * @author VMaximenko
 * 
 */
public class RestUser extends BaseObjectRest {

	public RestUser() {
		url = "/data/{user}";
	}

	private void setUpParams() {
		setUrlParam("user", StoreClient.getLogin());
	}
	
	public User retrieve() throws Exception {
		setUpParams();
		
		return doGetObject(User.class);
	}

	public User update(Object obj) throws Exception {
		return null;
	}
}
