package com.four_envelope.android.operation;

import com.four_envelope.android.activity.EnvelopeActivity;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.store.StoreExecution;

public class UpdateExecutionOperation extends AbstractOperation<Object, Execution> {

	private EnvelopeActivity mActivity;
	private String mEnvelopeBegin;
	private Execution mExecution;
	
	public UpdateExecutionOperation(UpdateListener context) {
		super(context);
		mActivity = (EnvelopeActivity) context;
	}

	@Override
	Execution process(Object... params) throws LocalizedException {
		mEnvelopeBegin = (String) params[0];
		mExecution = (Execution) params[1];

		return new StoreExecution( mEnvelopeBegin ).setData(mExecution);
	}

	@Override
	void onSuccess(Execution result) {
		mActivity.adapter.weekExecution.put( mEnvelopeBegin, result );
	}

	@Override
	void onClear() {
	}

}
