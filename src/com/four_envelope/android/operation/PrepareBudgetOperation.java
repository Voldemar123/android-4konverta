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

	private Integer position;
	private View view;
	private DailyBudgetAdapter adapter;
	
	public PrepareBudgetOperation(UpdateListener context) {
		super(context);
		adapter = (DailyBudgetAdapter) context;
	}

	@Override
	DailyBudget process(Object... params) throws LocalizedException {
		position = (Integer) params[0];
		view = (View) params[1];
		
		// look for envelope begin date
		String envelopeBegin = BudgetWork.calcEnvelopeBegin( adapter.dates[position] );
		
//check exist execution data
		Execution executionData;
//TODO for speed rewrite this code				
		if ( adapter.weekExecution.containsKey(envelopeBegin) )
			executionData = adapter.weekExecution.get(envelopeBegin);
		else {
			// get current week execution
			executionData = new StoreExecution(envelopeBegin).getData(false);
			
			adapter.weekExecution.put( envelopeBegin, executionData );
		}

		return BudgetWork.prepareDailyBudget( 
				adapter.dates[position], 
				executionData );
	}

	@Override
	void onSuccess(DailyBudget result) {
		adapter.budgets[position] = result;
		
		adapter.drawView(position, view);
   	 	view.postInvalidate();
	}

	@Override
	void onClear() {
	}

}
