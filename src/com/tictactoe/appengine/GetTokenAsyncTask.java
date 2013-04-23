package com.tictactoe.appengine;

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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.tictactoe.globals.TicTacToeGlobals;

public class GetTokenAsyncTask extends AsyncTask<Account, Void, AccountManagerFuture<Bundle>> {
    
    private static final String TAG = GetTokenAsyncTask.class.getSimpleName();
    
    private Context context;
    private GetTokenListener getTokenListener;
    
    public GetTokenAsyncTask(Context context, GetTokenListener getTokenListener) {
      this.context = context;
      this.getTokenListener = getTokenListener;
    }

    @Override
    protected AccountManagerFuture<Bundle> doInBackground(Account... accounts) {
        AccountManager accountManager = AccountManager.get(context);  
        Log.d(TAG, "Getting token for " + accounts[0] );
        AccountManagerFuture<Bundle> bundle = accountManager.getAuthToken(accounts[0], 
                    "ah", false, new GetAuthTokenCallback(), null);
        return bundle;
    }

    private class GetAuthTokenCallback implements AccountManagerCallback<Bundle> {
        public void run(AccountManagerFuture<Bundle> result) {
            Bundle bundle;
            try {
                bundle = (Bundle) result.getResult();
                Intent intent = (Intent)bundle.get(AccountManager.KEY_INTENT);
                if(intent != null) {
                    Log.d(TAG, "taking user input");
                    getTokenListener.onUserInputRequired(intent);
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

    private void onGetAuthToken(Bundle bundle) {
        String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        Log.d(TAG, "Got Token: " + authToken);
        new GetCookieTask().execute(authToken);
    }

    private class GetCookieTask extends AsyncTask<String, Void, Boolean> {
        protected Boolean doInBackground(String... tokens) {
            try {
                // "https://yourapp.appspot.com/_ah/login?continue=http://localhost/&auth= + tokens
                HttpGet httpGet = new HttpGet(
                        TicTacToeGlobals.PRODUCTION_URI + "/_ah/login?continue=" +
                                TicTacToeGlobals.APPENGINE_URI + "/&auth=" + tokens[0]);
                HttpResponse response = 
                            TicTacToeGlobals.getInstance().getHttpCient().execute(httpGet);

                StringBuffer sb = new StringBuffer();
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(inputStream, Charset.forName("utf-8")));
                String line = null;
                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }

                Log.d(TAG, "Response for GetCookie - " + sb);

                if(response.getStatusLine().getStatusCode() != 302) {
                    // Response should be a redirect
                    return false;
                }

                for(Cookie cookie : 
                    TicTacToeGlobals.getInstance().getHttpCient().getCookieStore().getCookies()) {
                    if(cookie.getName().equals("ACSID"))
                        return true;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                TicTacToeGlobals.getInstance().getHttpCient().
                        getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            getTokenListener.onTokenAcquired(result);
        }
    }

    public interface GetTokenListener {
        public void onTokenAcquired(Boolean result);
        public void onUserInputRequired(Intent intent);
    }
    
    // TODO build all queries this way
//    UrlBuilder builder = new UrlBuilder("https://graph.facebook.com/oauth/access_token")
//    .addParameter("client_id", application.getKey())
//    .addParameter("client_secret", application.getSecret())
//    .addParameter("redirect_uri", callbackURL)
//    .addParameter("code", code);

}
