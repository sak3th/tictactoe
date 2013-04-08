package com.tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.tictactoe.WelcomeFragment.OnLoginListener;
import com.tictactoe.model.Response;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AccountsFragment extends ListFragment  {

    private static final String TAG = "saketh";

    private static final String AUTH_TOKEN_TYPE = "Support multi player games";
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    private ArrayAdapter<Account> mAccountsAdapter;
    private AccountsListener mListener;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (AccountsListener) activity;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AccountManager accountManager = AccountManager.get(getActivity());
        Account[] accounts = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        mAccountsAdapter = new AccountsAdapter(getActivity().getApplicationContext(),
                R.layout.account_row, accounts);
        setListAdapter(mAccountsAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Account account = mAccountsAdapter.getItem(position);
        AccountManager accountManager = AccountManager.get(getActivity());	
        Log.d(TAG, " Account email is - " + account );
        accountManager.getAuthToken(account, "ah", false, new GetAuthTokenCallback(), null);
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

    private class GetAuthTokenCallback implements AccountManagerCallback {
        public void run(AccountManagerFuture result) {
            Bundle bundle;
            try {
                Log.d(TAG, "running " + result);
                bundle = (Bundle) result.getResult();
                Intent intent = (Intent)bundle.get(AccountManager.KEY_INTENT);
                if(intent != null) {
                    // User input required
                    startActivity(intent);
                    Log.d(TAG, "intent not null");
                } else {
                    onGetAuthToken(bundle);
                }
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    protected void onGetAuthToken(Bundle bundle) {
        String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        Log.d(TAG, "Got Token: " + authToken);
        new GetCookieTask().execute(authToken);
    }

    DefaultHttpClient httpClient = new DefaultHttpClient();
    
    private class GetCookieTask extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... tokens) {
            try {
                // Don't follow redirects
                httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
                // "https://yourapp.appspot.com/_ah/login?continue=http://localhost/&auth= + tokens
                HttpGet httpGet = new HttpGet(
                        TicTacToe.PRODUCTION_URI + "/_ah/login?continue=" +
                                TicTacToe.APPENGINE_URI + "/&auth=" + tokens[0]);
                HttpResponse response = httpClient.execute(httpGet);
                
                StringBuffer sb = new StringBuffer();
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(inputStream, Charset.forName("utf-8")));
                String line = null;
                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }
                
                Log.d(TAG, "Response from token - " + sb);
                
                if(response.getStatusLine().getStatusCode() != 302) {
                    // Response should be a redirect
                    return false;
                }

                for(Cookie cookie : httpClient.getCookieStore().getCookies()) {
                    if(cookie.getName().equals("ACSID"))
                        return true;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            new AuthenticatedRequestTask().execute(TicTacToe.APPENGINE_URI + "/reception/");
        }
    }

    private class AuthenticatedRequestTask extends AsyncTask<String, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(String... urls) {
            try {
                HttpGet httpGet = new HttpGet(urls[0]);
                return httpClient.execute(httpGet);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(HttpResponse response) {
            try {
                StringBuffer sb = new StringBuffer();
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(inputStream, Charset.forName("utf-8")));
                String line = null;
                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }
                Log.d(TAG, "Response from token - " + sb);
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public interface AccountsListener {
        public void onLogin(Response resp);
    }

}
