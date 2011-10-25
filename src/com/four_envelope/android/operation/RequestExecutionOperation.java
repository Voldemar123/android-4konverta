package com.four_envelope.android.operation;

import java.util.Date;

import com.four_envelope.android.activity.EnvelopeActivity;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.store.StoreExecution;
import com.four_envelope.android.store.StoreUser;

public class RequestExecutionOperation extends AbstractOperation<String, Execution> {

	private EnvelopeActivity mActivity;
	
	public RequestExecutionOperation(UpdateListener context) {
		super(context);
		mActivity = (EnvelopeActivity) context;
	}

	@Override
	Execution process(String... params) throws LocalizedException {
// get user properties
		BudgetWork.userData = new StoreUser().getData(mActivity.refreshContent);
	
// look for current envelope begin date    	
		Date envelopeBegin = BudgetWork.envelopeBegin( 
				mActivity.adapter.dates[mActivity.viewFlowPosition] ).getTime();

// get current week execution
		preloadExecution( 
				BudgetWork.formatDate(envelopeBegin) );
		
// preload previous and next week execution		
		preloadExecution(
				BudgetWork.calcPreviousEnvelopeBegin( envelopeBegin ) );
		preloadExecution(
				BudgetWork.calcNextEnvelopeBegin( envelopeBegin ) );
		
		return null;
	}

	private void preloadExecution(final String envelopeBegin) throws LocalizedException {
		mActivity.adapter.weekExecution.put( envelopeBegin, 
				new StoreExecution(envelopeBegin).getData( mActivity.refreshContent ) );
	}
	
	@Override
	void onSuccess(Execution result) {
	}

	@Override
	void onClear() {
	}
	
}