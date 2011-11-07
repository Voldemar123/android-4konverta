package com.four_envelope.android.activity;

import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.four_envelope.android.R;
import com.four_envelope.android.activity.Invoke.Extras;
import com.four_envelope.android.adapter.DailyBudgetAdapter;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.budget.DailyBudget;
import com.four_envelope.android.model.DailyExpense;
import com.four_envelope.android.model.Envelope;
import com.four_envelope.android.model.Person;
import com.four_envelope.android.operation.RequestExecutionOperation;
import com.four_envelope.android.operation.UpdateExecutionOperation;

/**
 * Show the day from weekly envelope with daily expenses  
 * @author VMaximenko
 *
 */
public class EnvelopeActivity extends BaseActivity {

	private ViewFlow mViewFlow;
	public DailyBudgetAdapter adapter;
	public int viewFlowPosition;
	
	private RequestExecutionOperation mRequestExecutionOperation;
	private UpdateExecutionOperation mUpdateExecutionOperation;
	
	
    public void onCreate(Bundle savedInstanceState) {
		mContentView = R.layout.envelope_day_flow_view;
		mMenuRes = R.menu.envelope_menu;
		mProgressRes = R.string.progress_envelope_msg;

		adapter = new DailyBudgetAdapter(this);

		mRequestExecutionOperation = new RequestExecutionOperation(this);
		mUpdateExecutionOperation = new UpdateExecutionOperation(this);
		
		setTitle( adapter.getTitle( adapter.getTodayId() ) );

		super.onCreate(savedInstanceState);
        
		mViewFlow = (ViewFlow) findViewById(R.id.envelope_viewflow);

		viewFlowPosition = adapter.getTodayId();
		
		mViewFlow.setOnViewSwitchListener(new ViewSwitchListener() {
		    public void onSwitched(View v, int position) {
		    	viewFlowPosition = position;
		    	setTitle( adapter.getTitle(position) );
		    }
		});
	}
    
	/* If your min SDK version is < 8 you need to trigger the onConfigurationChanged in ViewFlow manually, like this */	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mViewFlow.onConfigurationChanged(newConfig);
	}
    
    protected void requestPageContent() {
    	super.requestPageContent();

		adapter.clearDailyBudget();

    	mRequestExecutionOperation.execute();
	}
	
    protected void fillPageContent() {
	}
	
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
			case R.id.menu_today:
				mViewFlow.setSelection( adapter.getTodayId() );
				return true;

			case R.id.menu_back:
				mViewFlow.setSelection( mViewFlow.getSelectedItemPosition() - 1 );
				return true;

			case R.id.menu_forward:
				mViewFlow.setSelection( mViewFlow.getSelectedItemPosition() + 1 );
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// store updated person daily execution		
		if ( requestCode == Invoke.Extras.EXECUTION_POPUP_EDITOR ) {
            if (resultCode == RESULT_OK) {
            	Bundle extras = data.getExtras();

            	DailyExpense personDailyExpense = (DailyExpense) extras.getSerializable(Extras.PERSON_DAILY_EXPENSE_RESULT);
                Integer personId = extras.getInt(Extras.EXECUTION_PERSON_ID);

                int position = mViewFlow.getSelectedItemPosition();
                
                DailyBudget budget = adapter.getItem(position);
                Envelope envelope = budget.execution.getEnvelope();
                for (Person person : envelope.getPersons()) {
					if ( person.getId().equals(personId)) {
// replace daily expenses 						
						person.getDailyExpenses().clear();
						person.getDailyExpenses().add(personDailyExpense);
						break;
					}
				}

                budget.processEnvelope();
                
// save execution                
                mUpdateExecutionOperation.execute( envelope.getBegin(), budget.execution );
                
// refresh view flow                
                adapter.weekExecution.put( envelope.getBegin(), budget.execution );
                adapter.putItem(position, budget);
                
                View view = mViewFlow.getSelectedView();
                adapter.drawView(position, view);
                view.invalidate();
            }
        }
	
		super.onActivityResult(requestCode, resultCode, data);
	}
    
	public void onUpdate() {
		if ( mRequestExecutionOperation.isComplited() )
			mViewFlow.setAdapter(adapter, viewFlowPosition);

		if ( mUpdateExecutionOperation.isComplited() )
			return;
		
		super.onUpdate();
	}
	
}
