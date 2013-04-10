package com.tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tictactoe.model.Player;
import com.tictactoe.model.Response;

public class GetPlayerAsyncTask extends AsyncTask<String, Integer, HttpResponse> { 
    
    private static final String TAG = GetPlayerAsyncTask.class.getSimpleName();

    private PlayerInfoListener playerInfoListener;
    
    public GetPlayerAsyncTask(PlayerInfoListener playerInfoListener) {
        this.playerInfoListener = playerInfoListener;
    }

    @Override
    protected HttpResponse doInBackground(String... uris) {
        try {
            HttpGet httpGet = new HttpGet(uris[0]);
            return TicTacToeGlobals.getInstance().getHttpCient().execute(httpGet);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
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
            Log.d(TAG, "Response from GetPlayer - " + sb.toString());
            
            Type type = new TypeToken<Response<Player>>() {}.getType();
            Response<Player> resp = new Gson().fromJson(sb.toString(), type);
            
            //Response resp = new Gson().fromJson(sb.toString(), Response.class);
            playerInfoListener.onPlayerInfoAcquired((Player)resp.obj);                
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface PlayerInfoListener {
        public void onPlayerInfoAcquired(Player player);
    }

}

/*
 * {
 *   "status":"OK",
 *   "msg":"/_ah/logout?continue\u003dhttp%3A%2F%2Flocalhost%3A8888%2Flogout%2Fexample%40example.com",
 *   "obj":{"email":"example@example.com","userId":"0","age":0}
 * }
 */
