package com.four_envelope.android.adapter;

import java.util.ArrayList;

import com.four_envelope.android.activity.ExecutionPopupEditorActivity;
import com.four_envelope.android.model.Account;
import com.four_envelope.android.model.Expression;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class AccountSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    
	public class ViewHolder {
		TextView text;

		public Integer mAccountId;
	}
    
   private ExecutionPopupEditorActivity mActivity;

   private ArrayList<Account> items;
   private ArrayList<Integer> mAccounts;

	public AccountSpinnerAdapter(Context context, ArrayList<Account> items) {
		super();

		mActivity = (ExecutionPopupEditorActivity) context;
		this.items = items;
		
		mAccounts = new ArrayList<Integer>();
		for (Expression expression : mActivity.personDailyExpressions)
			mAccounts.add( expression.getAccount() );
	}
   
   public int getCount() {
      return items.size();
   }

   public Account getItem(int position) {
      return items.get(position);
   }

   public long getItemId(int position) {
      return position;
   }

   public boolean areAllItemsSelectable() {
      return false;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      return createResource(position, convertView, parent, android.R.layout.simple_spinner_item);
   }

   public View getDropDownView(int position, View convertView, ViewGroup parent) {
      return createResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
   }

   public View createResource(int position, View convertView, ViewGroup parent, int resource) {
		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(resource, null);
	         
	         holder = new ViewHolder();
	         holder.text = (TextView) convertView.findViewById(android.R.id.text1);
	         
	         convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

	  
		final Account o = getItem(position);
		if (o != null) {
			StringBuffer str = new StringBuffer( o.getName() );
			str.append(" [ ");
			str.append( o.getValue() );
			str.append(" ]");

			holder.text.setText( str.toString() );
			holder.mAccountId = o.getId();
			
// check person have daily expression for this account			
			if ( mAccounts.contains( o.getId() ) )
				holder.text.setTypeface(Typeface.DEFAULT_BOLD);
			else
				holder.text.setTypeface(Typeface.DEFAULT);
		}
      
      return convertView;
   }
}