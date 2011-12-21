package com.four_envelope.android.adapter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.taptwo.android.widget.TitleProvider;

import com.four_envelope.android.R;
import com.four_envelope.android.activity.EnvelopeActivity;
import com.four_envelope.android.activity.Invoke;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.budget.DailyBudget;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.operation.PrepareBudgetOperation;
import com.four_envelope.android.operation.UpdateListener;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Prepare view for daily budget for weekly envelope
 * @author VMaximenko
 *
 */
public class DailyBudgetAdapter extends BaseAdapter implements TitleProvider, UpdateListener {

	private LayoutInflater mInflater;
	public EnvelopeActivity activity;

	private static final int sDaysDepth = 10;
	private static final int sDaysSize = sDaysDepth * 2 + 1;
	
	public Date[] dates = new Date[sDaysSize];
	public DailyBudget[] budgets = new DailyBudget[sDaysSize];
	
	public HashMap<String, Execution> weekExecution = new HashMap<String, Execution>();
	

	private class ViewHolder {
		ProgressBar mProgressBar;

		View mExecutionLayoutEnvelopeSpentRemaining;
		View mExecutionLayoutAccountExecution;
		View mDateExecutionEmpty;
		
		TextView mEnvelopeSize;
		TextView mEnvelopeSpent;
		TextView mEnvelopeRemaining;
		TextView mEnvelopeRemainingMessage;
		TextView mTodaySpent;

		ListView mDateExecution;
		
		GridView mPersons;
	}
	
	public DailyBudgetAdapter(EnvelopeActivity context) {
		this.activity = context;
		
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		prepareDates();
	}
	
	@Override
	public DailyBudget getItem(int position) {
		return budgets[position];
	}

	public void putItem(int position, DailyBudget item) {
		budgets[position] = item;
	}
	
	@Override
	public long getItemId(int position) {
		return position; 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return drawView(position, convertView);
	}

	public View drawView(int position, View view) {
		ViewHolder holder = null;
		
		if( view == null ) {
			view = mInflater.inflate(R.layout.budget_day_view, null);
			
			holder = new ViewHolder();

			holder.mProgressBar = (ProgressBar) view.findViewById(R.id.execution_envelope_progress);
			
			holder.mExecutionLayoutEnvelopeSpentRemaining = (View) view.findViewById(R.id.execution_layout_envelope_spent_remaining);
			holder.mExecutionLayoutAccountExecution = (View) view.findViewById(R.id.execution_layout_account_execution);
			holder.mDateExecutionEmpty = (View) view.findViewById(R.id.date_execution_empty);

			holder.mEnvelopeSize = (TextView) view.findViewById(R.id.envelope_size);
			holder.mEnvelopeSpent = (TextView) view.findViewById(R.id.envelope_spent);
			holder.mEnvelopeRemaining = (TextView) view.findViewById(R.id.envelope_remaining);
			holder.mEnvelopeRemainingMessage = (TextView) view.findViewById(R.id.envelope_remaining_message);
			holder.mTodaySpent = (TextView) view.findViewById(R.id.today_spent);
			
			holder.mDateExecution = (ListView) view.findViewById(R.id.date_execution);

			holder.mPersons = (GridView) view.findViewById(R.id.envelope_persons);
			
			view.setTag(holder);
		} else
			holder = (ViewHolder) view.getTag();


		final DailyBudget o = getItem(position);
		if (o != null) {
			holder.mProgressBar.setVisibility(View.GONE);
			
			holder.mExecutionLayoutEnvelopeSpentRemaining.setVisibility(View.VISIBLE);
			holder.mExecutionLayoutAccountExecution.setVisibility(View.VISIBLE);
			
			holder.mEnvelopeSize.setText( o.getEnvelopeSize() ); 
			holder.mEnvelopeSpent.setText( o.getEnvelopeSpent() ); 
			holder.mTodaySpent.setText( o.getTodaySpent() ); 
			
// remaining from envelope
			holder.mEnvelopeRemaining.setText( o.getEnvelopeRemainingText() );
			
			if ( o.getEnvelopeRemaining() < 0 ) {
				holder.mEnvelopeRemaining.setTextColor(Color.RED);
				holder.mEnvelopeRemainingMessage.setText( activity.getText(R.string.envelope_remaining_overdraft) );
			}
			else {
				holder.mEnvelopeRemainingMessage.setText( activity.getText(R.string.envelope_remaining_left) );
				holder.mEnvelopeRemaining.setTextColor(Color.GREEN);
			}
			
			holder.mPersons.setAdapter(
	        		new ExecutionPersonAdapter( activity, o ) ); 

			// launch execution pop-up editor after click on person	
			holder.mPersons.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					ExecutionPersonAdapter.ViewHolder holder = (ExecutionPersonAdapter.ViewHolder) view.getTag();

					Invoke.User.executionPopupEditor(
							activity,
							activity.refreshContent, 
							holder.mPersonId, 
							holder.mPersonName, 
							holder.mDate);
				}

			});
			
        	holder.mDateExecution.setEmptyView(holder.mDateExecutionEmpty);
	        	
        	holder.mDateExecution.setAdapter( 
        			new ExecutionActualAdapter( 
        					activity, 
        					o.getActualActivities() ) );        
//				holder.mDateExecution.setOnItemClickListener(listener);
			holder.mDateExecution.setItemsCanFocus(true);
			
		}
		else {
			new PrepareBudgetOperation(this).execute(position, view);

			holder.mExecutionLayoutEnvelopeSpentRemaining.setVisibility(View.GONE);
			holder.mExecutionLayoutAccountExecution.setVisibility(View.GONE);
			
			holder.mProgressBar.setVisibility(View.VISIBLE);
		}
	
		return view;
	}

	/* (non-Javadoc)
	 * @see org.taptwo.android.widget.TitleProvider#getTitle(int)
	 */
	@Override
	public String getTitle(int position) {
		return BudgetWork.formatDateTitle( dates[position] );
	}

	@Override
	public int getCount() {
		return dates.length;
	}

	public int getTodayId() {
		return sDaysDepth;
	}

	public Date getTodayDate() {
		return dates[sDaysDepth];
	}
	
	public void clearDailyBudget() {
		for (int i = 0; i < budgets.length ; i++)
			budgets[i] = null;
		
		weekExecution.clear();
	}

	/**
	 * Prepare dates for navigation, to past and to future
	 */
	private void prepareDates() {
		Date today = new Date();

		Calendar calPast = Calendar.getInstance();
		Calendar calFuture = Calendar.getInstance();

		calPast.setTime(today);
		calFuture.setTime(today);

		dates[ sDaysDepth ] = calPast.getTime();
		for (int i = 1; i <= sDaysDepth; i++) {
			calPast.add( Calendar.DATE, -1 );
			dates[ sDaysDepth - i ] = calPast.getTime();

			calFuture.add( Calendar.DATE, 1 );
			dates[ sDaysDepth + i ] = calFuture.getTime();
		}
	}
	
	@Override
	public void onUpdate() {
	}

}
