package com.tictactoe;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tictactoe.globals.TicTacToeGlobals;

public class AccountsFragment extends ListFragment  {

    private ArrayAdapter<Account> mAccountsAdapter;
    private AccountsListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (AccountsListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AccountManager accountManager = AccountManager.get(getActivity());
        Account[] accounts = accountManager.getAccountsByType(TicTacToeGlobals.GOOGLE_ACCOUNT_TYPE);
        mAccountsAdapter = new AccountsAdapter(getActivity().getApplicationContext(),
                R.layout.account_row, accounts);
        setListAdapter(mAccountsAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onAccountSelected(mAccountsAdapter.getItem(position));
    }

    public class AccountsAdapter extends ArrayAdapter<Account> {
        private Account[] accounts;
        private Context context;
        private int resource;

        public AccountsAdapter(Context context, int resource, Account[] accounts) {
            super(context,resource, accounts);
            this.accounts = accounts;
            this.resource = resource;
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // First let's verify the convertView is not null
            if (convertView == null) {
                // This a new view we inflate the new layout
                LayoutInflater inflater = (LayoutInflater) 
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, parent, false);
            }
            // Now we can fill the layout with the right values
            TextView tv = (TextView) convertView.findViewById(R.id.textViewAccountName);
            tv.setText(accounts[position].name);
            return convertView;
        }
    }
    
    public interface AccountsListener {
        public void onAccountSelected(Account account);
    }

}
