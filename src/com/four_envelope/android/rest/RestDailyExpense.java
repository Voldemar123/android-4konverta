package com.four_envelope.android.rest;

import java.net.URLEncoder;

import com.four_envelope.android.model.DailyExpense;
import com.four_envelope.android.model.Expression;
import com.four_envelope.android.operation.LocalizedException;
import com.four_envelope.android.store.StoreClient;

/**
 * Work with person daily expense for date
 * @author VMaximenko
 */
public class RestDailyExpense extends BaseObjectRest {

	private String mPersonId;
	private String mDate;
	
	public RestDailyExpense(String personId, String date) {
		mPersonId = personId;
		mDate = date;
		
		url = "/data/{user}/dailyExpense/{personId}/{date}";
	}

	private void setUpParams() {
		setUrlParam("user", StoreClient.getLogin());
		setUrlParam("personId", mPersonId);
		setUrlParam("date", mDate);
	}
	
	public DailyExpense retrieve() throws LocalizedException {
		setUpParams();
		
		return doGetObject(DailyExpense.class);
	}

	public DailyExpense update(Object obj) throws LocalizedException {
		setUpParams();

		return doPostObject(
				DailyExpense.class,
				prepareContent( (Expression)obj ) );
	}

	private String prepareContent(Expression expression) {
		StringBuffer buf = new StringBuffer();
		
		buf.append("expression=");
		buf.append( URLEncoder.encode( expression.getValue() ) );
		buf.append("&account=");
		buf.append( expression.getAccount() );
		buf.append("&currency=");
		buf.append( expression.getCurrency() );
		
		return buf.toString();
	}


}