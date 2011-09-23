package com.four_envelope.android.store;

import com.four_envelope.android.Constants;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.rest.RestEnvelope;

/**
 * Work with stored envelope execution objects 
 * @author VMaximenko
 *
 */
public class StoreEnvelope extends BaseObjectStore {

	public StoreEnvelope(String envelopeBegin) {
		mRestClient = new RestEnvelope(envelopeBegin);

		mObjectPathName = Constants.APP_CACHE_PATH + StoreClient.getLogin() + "/execution";
		mObjectFileName = mObjectPathName + "/" + envelopeBegin;
	}
	
	public Execution getData(boolean update) throws Exception {
		mNeedUpdate = update;
		return (Execution) processObject();
	}

}
