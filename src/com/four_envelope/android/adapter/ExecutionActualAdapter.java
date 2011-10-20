package com.four_envelope.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.four_envelope.android.R;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.budget.DailyBudget;
import com.four_envelope.android.model.ActualExpense;
import com.four_envelope.android.model.ActualGoalCredit;
import com.four_envelope.android.model.ActualIncome;
import com.four_envelope.android.model.ExecutionActual;

/**
 * Daily envelope activities list item adapter 
 * @author VMaximenko
 */
public class ExecutionActualAdapter extends ArrayAdapter<ExecutionActual> {

	private ArrayList<ExecutionActual> items;
	private Context context;
	private final static int viewResourceId = R.layout.execution_actual_item;
	
	private class ViewHolder {
		CharSequence mActionText;
		String mDescriptionText;
		
		TextView mAction;
		TextView mPlanned;
		TextView mPlannedDate;
		TextView mPlannedDateFrom;
		TextView mActual;
		TextView mDescription;
	}


	private String getCurrencyValue(ExecutionActual actual) {
		String mCurrency = "";
		
		if (actual instanceof ActualIncome)
			mCurrency = ((ActualIncome) actual).getIncome().getCurrency().getValue();
		if (actual instanceof ActualExpense)
			mCurrency = ((ActualExpense) actual).getExpense().getCurrency().getValue();
		if (actual instanceof ActualGoalCredit)
			mCurrency = ((ActualGoalCredit) actual).getGoal().getCurrency().getValue();
		
		return mCurrency;
	}
	
	private CharSequence getPlannedValue(ExecutionActual actual) {
		return BudgetWork.formatMoney(actual.getPlanned(), getCurrencyValue(actual) );
	}
	private CharSequence getActualValue(ExecutionActual actual) {
		return BudgetWork.formatMoney( actual.getActual(), getCurrencyValue(actual) );
	}
	
	public ExecutionActualAdapter(Context context, ArrayList<ExecutionActual> items) {
		super(context, viewResourceId, items);

		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(	Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(viewResourceId, null);

			holder = new ViewHolder();

			holder.mAction = (TextView) convertView.findViewById(R.id.action);
			holder.mPlanned = (TextView) convertView.findViewById(R.id.planned);
			holder.mPlannedDate = (TextView) convertView.findViewById(R.id.planned_date);
			holder.mPlannedDateFrom = (TextView) convertView.findViewById(R.id.planned_date_from);
			holder.mActual = (TextView) convertView.findViewById(R.id.actual);
			holder.mDescription = (TextView) convertView.findViewById(R.id.description);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ExecutionActual o = items.get(position);
		if (o != null) {
			
			if (o instanceof ActualIncome) {
				holder.mActionText = this.context.getText(R.string.execution_actual_income);
				holder.mDescriptionText = ((ActualIncome) o).getIncome().getName();
			}
			if (o instanceof ActualExpense) {
				holder.mActionText = this.context.getText(R.string.execution_actual_expense);
				holder.mDescriptionText = ((ActualExpense) o).getExpense().getName();
			}
			if (o instanceof ActualGoalCredit) {
				holder.mActionText = this.context.getText(R.string.execution_actual_goal_credit);
				holder.mDescriptionText = ((ActualGoalCredit) o).getGoal().getName();
			}

			holder.mAction.setText( holder.mActionText );
			holder.mPlanned.setText( getPlannedValue(o) );
			
			if ( o.getActual() != null ) {

				if ( !o.getPlanned().equals( o.getActual() ) ) {
					holder.mActual.setVisibility(View.VISIBLE);
					holder.mActual.setText( getActualValue(o) );
					holder.mActual.setTextColor(Color.GRAY);
				}
				else
					holder.mActual.setVisibility(View.GONE);

				
				holder.mPlanned.setPaintFlags( holder.mPlanned.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );

				holder.mDescription.setTextColor(Color.GRAY);
			}
			else {
				holder.mActual.setVisibility(View.GONE);
				
				holder.mPlanned.setPaintFlags( holder.mPlanned.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG );
			}
			
			if ( DailyBudget.isPastPlannedDate(o) ) {
				holder.mPlannedDateFrom.setVisibility(View.VISIBLE);
				holder.mPlannedDate.setText( BudgetWork.formatDate( o.getPlannedDate() ) );
			}
			else {
				holder.mPlannedDateFrom.setVisibility(View.GONE);
				holder.mPlannedDate.setText("");
			}
			
			
			holder.mDescription.setText( holder.mDescriptionText );
			holder.mDescription.setSelected(true);
		}

		return convertView;
	}
	
}
