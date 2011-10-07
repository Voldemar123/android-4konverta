package com.four_envelope.android.operation;

import com.four_envelope.android.activity.StatusActivity;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.model.User;
import com.four_envelope.android.store.StoreUser;

public class RequestStatusOperation extends AbstractOperation<String, User> {

	private StatusActivity mActivity;
	
	public RequestStatusOperation(UpdateListener context) {
		super(context);
		mActivity = (StatusActivity) context;
	}

	@Override
	User process(String... params) throws LocalizedException {
		return new StoreUser().getData(mActivity.refreshContent);
	}

	@Override
	void onSuccess(User result) {
		BudgetWork.userData = result;
	}

	@Override
	void onClear() {
//		mActivity.refreshContent = false;
	}

}
