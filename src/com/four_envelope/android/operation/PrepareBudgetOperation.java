package com.four_envelope.android.operation;

import android.view.View;

import com.four_envelope.android.adapter.DailyBudgetAdapter;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.budget.DailyBudget;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.store.StoreExecution;

/**
 * Prepare daily budget and current week envelope data	
 * @author VMaximenko
 *
 */
public class PrepareBudgetOperation extends AbstractOperation<Object, DailyBudget> {

	private Integer mPosition;
	private View mView;
	private DailyBudgetAdapter mAdapter;
	
	public PrepareBudgetOperation(UpdateListener context) {
		super(context);
		mAdapter = (DailyBudgetAdapter) context;
	}

	@Override
	DailyBudget process(Object... params) throws LocalizedException {
		mPosition = (Integer) params[0];
		mView = (View) params[1];
		
		// look for envelope begin date
		String envelopeBegin = BudgetWork.calcEnvelopeBegin( mAdapter.dates[mPosition] );
		
//check exist execution data
		Execution executionData;
		if ( mAdapter.weekExecution.containsKey(envelopeBegin) )
			executionData = mAdapter.weekExecution.get(envelopeBegin);
		else {
			// get current week execution
			executionData = new StoreExecution(envelopeBegin).getData( mAdapter.activity.refreshContent );
			
			mAdapter.weekExecution.put( envelopeBegin, executionData );
		}

		return BudgetWork.prepareDailyBudget( 
				mAdapter.dates[mPosition], 
				executionData );
	}

	@Override
	void onSuccess(DailyBudget result) {
		mAdapter.budgets[mPosition] = result;
		
		mAdapter.drawView(mPosition, mView);
		mView.postInvalidate();
	}

	@Override
	void onClear() {
	}

}
