package com.four_envelope.android.store;

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

	public StoreDailyExpense(String personId, String date) throws LocalizedException {
		mRestClient = new RestDailyExpense(personId, date);

		mObjectPathName += "/dailyExpense/" + personId;
		mObjectFileName = mObjectPathName + "/" + date;
	}
	
	public DailyExpense getData(boolean update) throws LocalizedException {
		mNeedUpdate = update;
		return (DailyExpense) processObject();
	}

	public DailyExpense setData(Expression expression) throws LocalizedException {
		storeObject( mRestClient.update(expression) );
		
		return getData(false);
	}
}