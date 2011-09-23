package com.four_envelope.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.four_envelope.android.R;
import com.four_envelope.android.adapter.AccountAdapter;
import com.four_envelope.android.adapter.PersonAdapter;
import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.store.StoreClient;
import com.four_envelope.android.store.StoreUser;

/**
 * Show client status page, user properties, accounts
 * @author VMaximenko
 *
 */
public class StatusActivity extends BaseActivity {

	private TextView mCountry;
	private TextView mCurrency;
	private GridView mPersons;
	private ListView mAccounts;
	private TextView mAccountsEmpty;
	private TextView mAccountLogin;
	
	public void onCreate(Bundle savedInstanceState) {
		mContentView = R.layout.status_view;
		mMenuRes = R.menu.status_menu;
		mProgressRes = R.string.progress_status_msg;
		
		super.onCreate(savedInstanceState);
        
        mCountry = (TextView) findViewById(R.id.country);
        mCurrency = (TextView) findViewById(R.id.currency);
        mPersons = (GridView) findViewById(R.id.persons);
        mAccounts = (ListView) findViewById(R.id.accounts);
        mAccountsEmpty = (TextView) findViewById(R.id.accounts_empty);
        mAccountLogin = (TextView) findViewById(R.id.account_login);
	}

    protected class RequestStatusTask extends BaseRequestContentTask {
		
		protected Object doInBackground(Object... arg) {
			try {
				BudgetWork.userData = new StoreUser().getData(refreshContent);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
	}	    
	
    protected void requestPageContent() {
    	super.requestPageContent();
    	
    	new RequestStatusTask().execute();
	}
    
	void fillPageContent() {
		mAccountLogin.setText( StoreClient.getLogin() );
        mCountry.setText( BudgetWork.userData.getCountry().getValue() );
        mCurrency.setText( BudgetWork.userData.getCurrency().getValue() );
        
        mPersons.setAdapter(
        		new PersonAdapter( 
        				this, 
        				BudgetWork.userData.getPersons() ) );  
        
        if ( BudgetWork.userData.getAccounts() == null)
        	mAccountsEmpty.setVisibility(View.VISIBLE);
        else
        	mAccounts.setAdapter( 
        			new AccountAdapter( 
        					this, 
        					BudgetWork.userData.getAccounts() ) );        
	} 
	
}