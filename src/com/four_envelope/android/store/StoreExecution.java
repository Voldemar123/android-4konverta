package com.four_envelope.android.store;

import com.four_envelope.android.Constants;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.rest.RestExecution;

/**
 * Work with stored envelope execution objects 
 * @author VMaximenko
 *
 */
public class StoreExecution extends BaseObjectStore {

	public StoreExecution(String envelopeBegin) {
		mRestClient = new RestExecution(envelopeBegin);

		mObjectPathName = Constants.APP_CACHE_PATH + StoreClient.getLogin() + "/execution";
		mObjectFileName = mObjectPathName + "/" + envelopeBegin;
	}
	
	public Execution getData(boolean update) throws Exception {
		mNeedUpdate = update;
		return (Execution) processObject();
	}

	public Execution setData(Execution execution) throws Exception {
		storeObject( execution );
		
		return getData(false);
	}
}
