package com.four_envelope.android.adapter;

import java.util.ArrayList;

import com.four_envelope.android.model.Account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountAdapter extends ArrayAdapter<Account> {

	private ArrayList<Account> items;
	private final static int viewResourceId = android.R.layout.simple_list_item_2;

	private class ViewHolder {
		TextView mName;
		TextView mValue;
	}

	public AccountAdapter(Context context, ArrayList<Account> items) {
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

			holder.mName = (TextView) convertView.findViewById(android.R.id.text1);
			holder.mValue = (TextView) convertView.findViewById(android.R.id.text2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Account o = items.get(position);
		if (o != null) {
			holder.mName.setText(o.getName());
			holder.mValue.setText(o.getValue());
		}

		return convertView;
	}

}
