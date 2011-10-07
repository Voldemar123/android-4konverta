package com.four_envelope.android.store;

import android.util.Log;

import com.four_envelope.android.Constants;
import com.four_envelope.android.model.DailyExpense;
import com.four_envelope.android.model.Expression;
import com.four_envelope.android.operation.LocalizedException;
import com.four_envelope.android.rest.RestDailyExpense;

/**
 * Work with stored person daily expense objects 
 * @author VMaximenko
 *
 */
public class StoreDailyExpense extends BaseObjectStore {

	public StoreDailyExpense(String personId, String date) {
		mRestClient = new RestDailyExpense(personId, date);

		mObjectPathName = Constants.APP_CACHE_PATH + StoreClient.getLogin() + "/dailyExpense/" + personId;
		mObjectFileName = mObjectPathName + "/" + date;
	}
	
	public DailyExpense getData() throws LocalizedException {
		Log.i(getClass().getSimpleName(), "getData nObjectFileName " + mObjectFileName);
		
		return (DailyExpense) processObject();
	}

	public DailyExpense setData(Expression expression) throws LocalizedException {
		Log.i(getClass().getSimpleName(), "setData nObjectFileName " + mObjectFileName);
		
		storeObject( mRestClient.update(expression) );
		
		return getData();
	}
}