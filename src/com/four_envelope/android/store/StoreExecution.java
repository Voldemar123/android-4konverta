package com.four_envelope.android.store;

import com.four_envelope.android.model.Execution;
import com.four_envelope.android.operation.LocalizedException;
import com.four_envelope.android.rest.RestExecution;

/**
 * Work with stored envelope execution objects 
 * @author VMaximenko
 *
 */
public class StoreExecution extends BaseObjectStore {

	public StoreExecution(String envelopeBegin) throws LocalizedException {
		mRestClient = new RestExecution(envelopeBegin);

		mObjectPathName += "/execution";
		mObjectFileName = mObjectPathName + "/" + envelopeBegin;
	}
	
	public Execution getData(boolean update) throws LocalizedException {
		mNeedUpdate = update;
		return (Execution) processObject();
	}

	public Execution setData(Execution execution) throws LocalizedException {
		storeObject( execution );
		
		return getData(false);
	}
}
