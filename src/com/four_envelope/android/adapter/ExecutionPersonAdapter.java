package com.four_envelope.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.four_envelope.android.R;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.model.Person;

/**
 * Show person list with it daily expenses 
 * @author VMaximenko
 *
 */
public class ExecutionPersonAdapter extends ArrayAdapter<Person> {
	
	private ArrayList<Person> items;
	private final static int viewResourceId = R.layout.envelope_persons_list_item;

	protected class ViewHolder {
		TextView mName;
		TextView mSum;
		
		Integer mPersonId;
		String mPersonName;
		String mDate;
	}
	
	public ExecutionPersonAdapter(Context context, ArrayList<Person> items) {
		super(context, viewResourceId, items);
		this.items = items;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(	Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(viewResourceId, null);

			holder = new ViewHolder();

			holder.mName = (TextView) convertView.findViewById(R.id.envelope_person_name);
			holder.mSum = (TextView) convertView.findViewById(R.id.envelope_person_sum);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Person o = items.get(position);
		if (o != null) {
// one day person sum expense	
			holder.mSum.setText( BudgetWork.getPersonDailyExpense(o) );

			holder.mName.setText( o.getName() );
			if ( BudgetWork.checkPersonHasntSetDailyExpense(o) )
				holder.mName.setTextColor(Color.RED);
			
			holder.mPersonId = o.getId();
			holder.mPersonName = o.getName();
			holder.mDate = BudgetWork.getPersonExpenseDate(o);
		}

		return convertView;
    }	

}
