package com.four_envelope.android.activity;

import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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
import com.four_envelope.android.store.StoreExecution;
import com.four_envelope.android.store.StoreUser;

/**
 * Show the day from weekly envelope with daily expenses  
 * @author VMaximenko
 *
 */
public class EnvelopeActivity extends BaseActivity {

	private ViewFlow mViewFlow;
	private DailyBudgetAdapter mAdapter;
	private int mViewFlowPosition;
	
    public void onCreate(Bundle savedInstanceState) {
		mContentView = R.layout.envelope_day_flow_view;
		mMenuRes = R.menu.envelope_menu;
		mProgressRes = R.string.progress_envelope_msg;
		
		mAdapter = new DailyBudgetAdapter(this);

		setTitle( mAdapter.getTitle( mAdapter.getTodayId() ) );

		super.onCreate(savedInstanceState);
        
		mViewFlow = (ViewFlow) findViewById(R.id.envelope_viewflow);

		mViewFlowPosition = mAdapter.getTodayId();
		
		mViewFlow.setOnViewSwitchListener(new ViewSwitchListener() {
		    public void onSwitched(View v, int position) {
		    	mViewFlowPosition = position;
		    	setTitle( mAdapter.getTitle(position) );
		    }
		});
		
	}
    
	/* If your min SDK version is < 8 you need to trigger the onConfigurationChanged in ViewFlow manually, like this */	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mViewFlow.onConfigurationChanged(newConfig);
	}
    
    protected class RequestEnvelopeTask extends BaseRequestContentTask {
		
		protected Object doInBackground(Object... arg) {
			
			try {
// get user properties
				BudgetWork.userData = new StoreUser().getData(refreshContent);
			
// look for envelope begin date    			
				String envelopeBegin = BudgetWork.calcEnvelopeBegin( mAdapter.getTodayDate() );
				
// get current week execution 
    			new StoreExecution(envelopeBegin).getData(refreshContent);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		
	}	    
	
    protected void requestPageContent() {
    	super.requestPageContent();
    	
    	new RequestEnvelopeTask().execute();
	}
	
	void fillPageContent() {
		mAdapter.clearDailyBudget();
		mViewFlow.setAdapter(mAdapter, mViewFlowPosition);
	}
	
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
			case R.id.menu_today:
				mViewFlow.setSelection( mAdapter.getTodayId() );
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
		Log.i(getClass().getSimpleName(), "onActivityResult");
		Log.i(getClass().getSimpleName(), Integer.toString(requestCode));
		Log.i(getClass().getSimpleName(), Integer.toString(resultCode));

// store updated person daily execution		
		if ( requestCode == Invoke.Extras.EXECUTION_POPUP_EDITOR ) {
            if (resultCode == RESULT_OK) {
            	Bundle extras = data.getExtras();

            	DailyExpense personDailyExpense = (DailyExpense) extras.getSerializable(Extras.PERSON_DAILY_EXPENSE_RESULT);
                Integer personId = extras.getInt(Extras.EXECUTION_PERSON_ID);

                int position = mViewFlow.getSelectedItemPosition();
                
                DailyBudget budget = mAdapter.getItem(position);
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
                
                try {
					new StoreExecution( envelope.getBegin() ).setData( budget.execution );
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                mAdapter.putItem(position, budget);
                
                View view = mViewFlow.getSelectedView();
                mAdapter.drawView(position, view);
                view.invalidate();
                
            	Log.i(getClass().getSimpleName(), Float.toString(personDailyExpense.getSum()));
            	
//            	requestPageContent();
            }
        }
	
		super.onActivityResult(requestCode, resultCode, data);
	}
    
}
