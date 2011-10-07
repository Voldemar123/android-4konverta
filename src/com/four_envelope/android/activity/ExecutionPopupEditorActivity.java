package com.four_envelope.android.activity;

import com.four_envelope.android.R;
import com.four_envelope.android.activity.Invoke.Extras;
import com.four_envelope.android.adapter.AccountSpinnerAdapter;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.budget.DailyBudget;
import com.four_envelope.android.model.Account;
import com.four_envelope.android.model.DailyExpense;
import com.four_envelope.android.model.Expression;
import com.four_envelope.android.operation.RequesDailyExpenseOperation;
import com.four_envelope.android.store.StoreDailyExpense;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ExecutionPopupEditorActivity extends BaseActivity {

	private Spinner mAccounts;
	private EditText mEditorExpression;
	private TextView mExpensePersonName;
	
	private Integer mPersonId;
	private String mDate, mPersonName;
	public DailyExpense personDailyExpense;
	private Expression mEditExpression;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		mContentView = R.layout.execution_popup_editor;

        Bundle extras = getIntent().getExtras();
        
        mPersonId = extras.getInt(Extras.EXECUTION_PERSON_ID);
        mPersonName = extras.getString(Extras.EXECUTION_PERSON_NAME);
        mDate = extras.getString(Extras.EXECUTION_DATE);
        
        super.onCreate(savedInstanceState);
        
        mAccounts = (Spinner) findViewById(R.id.execution_popup_editor_account_spinner);
        mEditorExpression = (EditText) findViewById(R.id.execution_popup_editor_expression);
        mExpensePersonName = (TextView) findViewById(R.id.execution_person);
    }
    
    protected void requestPageContent() {
    	super.requestPageContent();
    	
    	new RequesDailyExpenseOperation(this).execute( mPersonId, mDate );
	}
    
	void fillPageContent() {
		mExpensePersonName.setText(mPersonName);
		
// default account expression
		String defaultCurrency = BudgetWork.userData.getCurrency().getId();
		Log.i(getClass().getSimpleName(), personDailyExpense.getDefaultAccount().toString());
		
		if ( personDailyExpense.getExpressions() != null) {
			for (Expression expression : personDailyExpense.getExpressions()) {
				Log.i(getClass().getSimpleName(), expression.getAccount().toString());
				
				if ( expression.getAccount().equals( personDailyExpense.getDefaultAccount() )) {
					Log.i(getClass().getSimpleName(), expression.getValue());

					mEditExpression = expression;
					defaultCurrency = expression.getCurrency();
					mEditorExpression.setText( expression.getValue() );
					break;
				}
			}
		}
		else {
// day without user expenses  			
			mEditExpression = new Expression();
			
			mEditExpression.setCurrency(defaultCurrency);
			mEditExpression.setAccount(personDailyExpense.getDefaultAccount());
		}

        mAccounts.setAdapter( new AccountSpinnerAdapter( 
				this, 
				BudgetWork.userData.getAccounts() ) ); 

// select default account currency        
        for (int i = 0; i < mAccounts.getCount(); i++) {
        	Account account = (Account) mAccounts.getItemAtPosition(i);
            if ( account.getCurrency().getId().equals( defaultCurrency )) {
            	mAccounts.setSelection(i);
            	break;
            }
        }
	}

	public void saveExecution(View view) { 
		Log.i(getClass().getSimpleName(), "saveExecution");
		showProgress(R.string.progress_msg_save);
		new UpdateExecutionTask().execute();
	}

	public void closeExecution(View view) { 
		finish();
	}
	
	
// Update execution data
	private class UpdateExecutionTask extends AsyncTask<Object, Object, Object> {
		
		@Override
		protected DailyBudget doInBackground(Object... arg) {

			Log.i(getClass().getSimpleName(), "---");

			try {
// current edit daily selected currency expression				
				mEditExpression.setValue( mEditorExpression.getText().toString() );
				
// update daily expense				
				personDailyExpense = new StoreDailyExpense( mPersonId.toString(), mDate ).setData(mEditExpression);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Object result) {

			Log.i(getClass().getSimpleName(), "+++");

			hideProgress();

			Intent intent = new Intent();
			
			intent.putExtra( Extras.PERSON_DAILY_EXPENSE_RESULT, personDailyExpense );
			intent.putExtra( Extras.EXECUTION_PERSON_ID, mPersonId );
			intent.putExtra( Extras.EXECUTION_DATE, mDate );
			
			setResult( RESULT_OK, intent );
			
			finish();
		}

	}
}
