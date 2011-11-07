package com.four_envelope.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.four_envelope.android.R;
import com.four_envelope.android.activity.Invoke.Extras;
import com.four_envelope.android.adapter.AccountAdapter;
import com.four_envelope.android.adapter.PersonAdapter;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.operation.RequestStatusOperation;
import com.four_envelope.android.store.PersonImage;
import com.four_envelope.android.store.StoreClient;

/**
 * Show client status page, user properties, accounts
 * @author VMaximenko
 *
 */
public class StatusActivity extends BaseActivity {

	private TextView mCurrency;
	private GridView mPersons;
	private ListView mAccounts;
	private View mAccountsEmpty;
	private TextView mAccountLogin;

	private Integer mSelectedPersonId;
	
	
	public void onCreate(Bundle savedInstanceState) {
		mContentView = R.layout.status_view;
		mMenuRes = R.menu.status_menu;
		mProgressRes = R.string.progress_status_msg;
		
		super.onCreate(savedInstanceState);
        
        mCurrency = (TextView) findViewById(R.id.currency);
        mPersons = (GridView) findViewById(R.id.persons);
        mAccounts = (ListView) findViewById(R.id.accounts);
        mAccountsEmpty = (View) findViewById(R.id.accounts_empty);
        mAccountLogin = (TextView) findViewById(R.id.account_login);
	}

    protected void requestPageContent() {
    	super.requestPageContent();
    	
    	new RequestStatusOperation(this).execute();
	}
    
	protected void fillPageContent() {
// check have requested data
		if (BudgetWork.userData == null)
			return;

		mAccountLogin.setText( StoreClient.getLogin() );
		
        mCurrency.setText( BudgetWork.userData.getCurrency().getValue() );
        
        mPersons.setAdapter(
        		new PersonAdapter( 
        				this, 
        				BudgetWork.userData.getPersons() ) );  
        
// launch select image from gallery after click on person	
		mPersons.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				PersonAdapter.ViewHolder holder = (PersonAdapter.ViewHolder) view.getTag();

				mSelectedPersonId = holder.personId;
				
				Invoke.User.selectPersonImage(
						StatusActivity.this,
						holder.personId);
			}

		});
        
        
		mAccounts.setEmptyView(mAccountsEmpty);
		mAccounts.setAdapter(
				new AccountAdapter(
						this, 
						BudgetWork.userData.getAccounts() ) );
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == Activity.RESULT_OK ) {
            if ( requestCode == Extras.SELECT_PICTURE ) {
//              PersonImage.save( mSelectedPersonId, data.getData() ); 
                PersonImage.save( mSelectedPersonId, PersonImage.getTempUri(mSelectedPersonId) ); 
                
                fillPageContent();
            }
        }
    }
}