package com.four_envelope.android.operation;

import com.four_envelope.android.activity.ExecutionPopupEditorActivity;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.model.DailyExpense;
import com.four_envelope.android.store.StoreDailyExpense;
import com.four_envelope.android.store.StoreUser;

public class RequesDailyExpenseOperation extends AbstractOperation<Object, DailyExpense> {

	private ExecutionPopupEditorActivity mActivity;
	private Integer mPersonId;
	private String mDate;
	
	public RequesDailyExpenseOperation(UpdateListener context) {
		super(context);
		mActivity = (ExecutionPopupEditorActivity) context;
	}

	@Override
	DailyExpense process(Object... params) throws LocalizedException {
		mPersonId = (Integer) params[0];
		mDate = (String) params[1];
		
// get user properties
		BudgetWork.userData = new StoreUser().getData(mActivity.refreshContent);
			
// get person daily expense
		return new StoreDailyExpense( mPersonId.toString(), mDate ).getData(mActivity.refreshContent);
	}

	@Override
	void onSuccess(DailyExpense result) {
		mActivity.personDailyExpense = result;
	}

	@Override
	void onClear() {
	}

}