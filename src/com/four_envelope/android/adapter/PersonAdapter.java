package com.four_envelope.android.adapter;

import java.util.ArrayList;

import com.four_envelope.android.R;
import com.four_envelope.android.model.Person;
import com.four_envelope.android.store.PersonImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonAdapter extends ArrayAdapter<Person> {

	private ArrayList<Person> items;
	private final static int viewResourceId = R.layout.status_persons_list_item;
	
	public class ViewHolder {
		TextView mName;
		ImageView mIcon;
		
		public Integer personId;
	}
	
	public PersonAdapter(Context context, ArrayList<Person> items) {
		super(context, viewResourceId, items);
		this.items = items;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(	Context.LAYOUT_INFLATER_SERVICE );
			convertView = vi.inflate( viewResourceId, null );

			holder = new ViewHolder();

			holder.mName = (TextView) convertView.findViewById(R.id.name);
			holder.mIcon = (ImageView) convertView.findViewById(R.id.icon);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Person o = items.get(position);
		if (o != null) {
			holder.personId = o.getId();
			holder.mName.setText( o.getName() );
			PersonImage.load( holder.mIcon, holder.personId );
		}

		return convertView;
    }	
}
