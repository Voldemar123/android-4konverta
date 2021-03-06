package com.four_envelope.android.rest;

import com.four_envelope.android.model.Execution;
import com.four_envelope.android.operation.LocalizedException;
import com.four_envelope.android.store.StoreClient;

/**
 * Work with envelope execution information on server
 * 
 * @author VMaximenko
 * 
 */
public class RestExecution extends BaseObjectRest {

	private String mEnvelopeBegin;
	
	public RestExecution(String envelopeBegin) throws LocalizedException {
		mEnvelopeBegin = envelopeBegin;
		
		url = "/data/{user}/execution/{envelopeBegin}";
	}

	public void setEnvelopeBegin(String envelopeBegin) {
		this.mEnvelopeBegin = envelopeBegin;
	}

	private void setUpParams() {
		setUrlParam("user", StoreClient.getLogin());
		setUrlParam("envelopeBegin", mEnvelopeBegin);
	}
	
	public Execution retrieve() throws LocalizedException {
		setUpParams();
		
		return doGetObject(Execution.class);
	}

	public Execution update(Object obj) throws LocalizedException {
		return null;
	}

}
