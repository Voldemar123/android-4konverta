package com.four_envelope.android.operation;

import com.four_envelope.android.activity.EnvelopeActivity;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.store.StoreExecution;
import com.four_envelope.android.store.StoreUser;

public class RequestExecutionOperation extends AbstractOperation<String, Execution> {

	private EnvelopeActivity mActivity;
	private String mEnvelopeBegin;
	
	public RequestExecutionOperation(UpdateListener context) {
		super(context);
		mActivity = (EnvelopeActivity) context;
	}

	@Override
	Execution process(String... params) throws LocalizedException {
// get user properties
		BudgetWork.userData = new StoreUser().getData(mActivity.refreshContent);
	
// look for envelope begin date    			
		mEnvelopeBegin = BudgetWork.calcEnvelopeBegin( mActivity.adapter.dates[mActivity.viewFlowPosition] );
		
// get current week execution 
		return new StoreExecution(mEnvelopeBegin).getData(mActivity.refreshContent);
	}

	@Override
	void onSuccess(Execution result) {
		mActivity.adapter.weekExecution.put( mEnvelopeBegin, result );
	}

	@Override
	void onClear() {
//		mActivity.refreshContent = false;
	}

}