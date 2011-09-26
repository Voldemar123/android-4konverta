package com.four_envelope.android.adapter;

import java.util.Calendar;
import java.util.Date;

import org.taptwo.android.widget.TitleProvider;

import com.four_envelope.android.R;
import com.four_envelope.android.activity.Invoke;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.budget.DailyBudget;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.store.StoreExecution;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
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
public class DailyBudgetAdapter extends BaseAdapter implements TitleProvider {

	private LayoutInflater mInflater;
	private Context context;

	private static final int daysDepth = 10;
	private static final int daysSize = daysDepth * 2 + 1;
	
	private static Date[] dates = new Date[ daysSize ];
	private static DailyBudget[] budgets = new DailyBudget[ daysSize ];
	

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
	
	public DailyBudgetAdapter(Context context) {
		this.context = context;
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
			Log.i(getClass().getSimpleName(), "+++");
			Log.i(getClass().getSimpleName(), dates[position].toLocaleString());		
			
			holder.mProgressBar.setVisibility(View.GONE);
			
			holder.mExecutionLayoutEnvelopeSpentRemaining.setVisibility(View.VISIBLE);
			holder.mExecutionLayoutAccountExecution.setVisibility(View.VISIBLE);
			
			holder.mEnvelopeSize.setText( o.getEnvelopeSize() ); 
			holder.mEnvelopeSpent.setText( o.getEnvelopeSpent() ); 
			holder.mTodaySpent.setText( o.getTodaySpent() ); 
			
// remaining from envelope
			holder.mEnvelopeRemaining.setText( o.getEnvelopeRemainingText() );
			
			if ( o.execution.getEnvelope().getSize() < o.getEnvelopeRemaining()  ) {
				holder.mEnvelopeRemaining.setTextColor(Color.RED);
				holder.mEnvelopeRemainingMessage.setText( context.getText(R.string.envelope_remaining_overdraft) );
			}
			else 
				holder.mEnvelopeRemaining.setTextColor(Color.GREEN);
			
			holder.mPersons.setAdapter(
	        		new ExecutionPersonAdapter( context, o ) ); 
			holder.mPersons.setOnItemClickListener(ExecutionPersonListener);

			
        	holder.mDateExecution.setEmptyView(holder.mDateExecutionEmpty);
	        	
        	holder.mDateExecution.setAdapter( 
        			new ExecutionActualAdapter( 
        					context, 
        					o.getActualActivities() ) );        
//				holder.mDateExecution.setOnItemClickListener(listener);
			holder.mDateExecution.setItemsCanFocus(true);
			
		}
		else {
			Log.i(getClass().getSimpleName(), "---");
			
			new PrepareBudgetTask().execute(position, view);

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
		return BudgetWork.formatDate( dates[position] );
	}

	@Override
	public int getCount() {
		return dates.length;
	}

	public int getTodayId() {
		return daysDepth;
	}

	public Date getTodayDate() {
		return dates[daysDepth];
	}
	
	public void clearDailyBudget() {
		for (int i = 0; i < budgets.length ; i++)
			budgets[i] = null;
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

		dates[ daysDepth ] = calPast.getTime();
		for (int i = 1; i <= daysDepth; i++) {
			calPast.add( Calendar.DATE, -1 );
			dates[ daysDepth - i ] = calPast.getTime();

			calFuture.add( Calendar.DATE, 1 );
			dates[ daysDepth + i ] = calFuture.getTime();
		}
	}
	
// launch execution pop-up editor after click on person	
	private OnItemClickListener ExecutionPersonListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			ExecutionPersonAdapter.ViewHolder holder = (ExecutionPersonAdapter.ViewHolder) view.getTag();

			Log.i(getClass().getSimpleName(), holder.mPersonId.toString());
			Log.i(getClass().getSimpleName(), holder.mName.getText().toString());
			
			Invoke.User.executionPopupEditor(
					(Activity)context, 
					holder.mPersonId, 
					holder.mPersonName, 
					holder.mDate);
		}

	};	
	
/**
 * Prepare daily budget and current week envelope data	
 * @author VMaximenko
 *
 */
	private class PrepareBudgetTask extends AsyncTask<Object, Object, Object> {
		
		private Integer position;
		private View view;
		
		@Override
		protected DailyBudget doInBackground(Object... arg) {
			position = (Integer) arg[0];
			view = (View) arg[1];

			try {
				// look for envelope begin date
				String envelopeBegin = BudgetWork.calcEnvelopeBegin( dates[position] );
				
				// get current week execution
				Execution executionData = new StoreExecution(envelopeBegin).getData(false);

				return BudgetWork.prepareDailyBudget( dates[position], executionData );

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		protected void onPostExecute(Object result) {
	    	 budgets[position] = (DailyBudget) result;
	    	 
	    	 drawView(position, view);
	    	 
	    	 view.postInvalidate();
	     }

	}	
}
