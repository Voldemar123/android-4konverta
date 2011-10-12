package com.four_envelope.android.operation;

import com.four_envelope.android.activity.ExecutionPopupEditorActivity;
import com.four_envelope.android.model.DailyExpense;
import com.four_envelope.android.store.StoreDailyExpense;

public class UpdateDailyExpenseOperation extends AbstractOperation<Object, DailyExpense> {

	private ExecutionPopupEditorActivity mActivity;
	private Integer mPersonId;
	private String mDate;
	
	public UpdateDailyExpenseOperation(UpdateListener context) {
		super(context);
		mActivity = (ExecutionPopupEditorActivity) context;
	}

	@Override
	DailyExpense process(Object... params) throws LocalizedException {
		mPersonId = (Integer) params[0];
		mDate = (String) params[1];

// update daily expense				
		return new StoreDailyExpense( mPersonId.toString(), mDate ).setData(mActivity.editExpression);
	}

	@Override
	void onSuccess(DailyExpense result) {
		mActivity.personDailyExpense = result;
	}

	@Override
	void onClear() {
	}

}
