package com.four_envelope.android.activity;

import java.util.ArrayList;

import com.four_envelope.android.R;
import com.four_envelope.android.activity.Invoke.Extras;
import com.four_envelope.android.adapter.AccountSpinnerAdapter;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.model.Account;
import com.four_envelope.android.model.DailyExpense;
import com.four_envelope.android.model.Expression;
import com.four_envelope.android.operation.RequesDailyExpenseOperation;
import com.four_envelope.android.operation.UpdateDailyExpenseOperation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
	public Expression editExpression;
	public ArrayList<Expression> personDailyExpressions;
	
	private UpdateDailyExpenseOperation mUpdateDailyExpense;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		mContentView = R.layout.execution_popup_editor;

        Bundle extras = getIntent().getExtras();
        
        refreshContent = extras.getBoolean(Extras.EXECUTION_REFRESH);
        mPersonId = extras.getInt(Extras.EXECUTION_PERSON_ID);
        mPersonName = extras.getString(Extras.EXECUTION_PERSON_NAME);
        mDate = extras.getString(Extras.EXECUTION_DATE);
        
        super.onCreate(savedInstanceState);
        
        mAccounts = (Spinner) findViewById(R.id.execution_popup_editor_account_spinner);
        mEditorExpression = (EditText) findViewById(R.id.execution_popup_editor_expression);
        mExpensePersonName = (TextView) findViewById(R.id.execution_person);
        
        mUpdateDailyExpense = new UpdateDailyExpenseOperation(this);
    }
    
    protected void requestPageContent() {
    	super.requestPageContent();
    	
    	new RequesDailyExpenseOperation(this).execute( mPersonId, mDate );
	}
    
	void fillPageContent() {
		mExpensePersonName.setText(mPersonName);
		
// default account expression
		String defaultCurrency = BudgetWork.userData.getCurrency().getId();

		personDailyExpressions = personDailyExpense.getExpressions();
		if ( personDailyExpressions == null || personDailyExpressions.size() == 0 ) {
// day without user expenses  			
			editExpression = new Expression();
				
			editExpression.setCurrency(defaultCurrency);
			editExpression.setAccount( personDailyExpense.getDefaultAccount() );
			
			personDailyExpressions = new ArrayList<Expression>();
		}
		else {
			Expression expression = personDailyExpressions.get(0);

			editExpression = expression;
			mEditorExpression.setText( expression.getValue() );
		}
		
        mAccounts.setAdapter( new AccountSpinnerAdapter( 
				this, 
				BudgetWork.userData.getAccounts() ) ); 

// select account with expression        
        for (int i = 0; i < mAccounts.getCount(); i++) {
        	Account account = (Account) mAccounts.getItemAtPosition(i);
            if ( account.getId().equals( editExpression.getAccount() )) {
            	mAccounts.setSelection(i);
            	break;
            }
        }
        
        mAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
            	AccountSpinnerAdapter.ViewHolder holder = (AccountSpinnerAdapter.ViewHolder) view.getTag();

// show expense for select account
            	String expressionText = "";
            	for (Expression expression : personDailyExpressions)
            		if ( expression.getAccount().equals( holder.mAccountId ) ) {
            			expressionText = expression.getValue();
            			break;
            		}
            	
            	mEditorExpression.setText(expressionText);
            } 

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            } 
        }); 
	}

	public void saveExecution(View view) { 
		showProgress(R.string.progress_msg_save);
		
// current edit daily selected currency expression				
		editExpression.setValue( mEditorExpression.getText().toString() );
		
		mUpdateDailyExpense.execute( mPersonId, mDate );
	}

	public void closeExecution(View view) { 
		finish();
	}

	public void updateExecution() { 
		hideProgress();

		Intent intent = new Intent();
		
		intent.putExtra( Extras.PERSON_DAILY_EXPENSE_RESULT, personDailyExpense );
		intent.putExtra( Extras.EXECUTION_PERSON_ID, mPersonId );
		intent.putExtra( Extras.EXECUTION_DATE, mDate );
		
		setResult( RESULT_OK, intent );
		
		finish();
	}
	
	public void onUpdate() {
		if ( mUpdateDailyExpense.isComplited() )
			updateExecution();
		
		super.onUpdate();
	}
	
}
