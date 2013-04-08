package com.tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import com.google.gson.Gson;
import com.tictactoe.model.Response;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

public class LoginTask extends AsyncTask<String, Integer, Response> {
	
	Message msg;
	public LoginTask(Message msg) {
		this.msg = msg;
	}

	public static LoginTask login(Message msg, String uri) {
		LoginTask loginTask = new LoginTask(msg);
		loginTask.execute(uri);
		return loginTask;
	}
	
	@Override
	protected Response doInBackground(String... uris) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uris[0]);
		HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			StringBuffer sb = new StringBuffer();
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader buffer = new BufferedReader(
						new InputStreamReader(inputStream, Charset.forName("utf-8")));
			String line = null;
			while ((line = buffer.readLine()) != null) {
			    sb.append(line);
			}
			//String jsonString = sb.toString();
			Log.d("saketh", "returning respose");
			return new Gson().fromJson(sb.toString(), Response.class);
		} catch (ClientProtocolException cpe) {
			Log.d("saketh","cpe");
		} catch (IOException ioe) {
			Log.d("saketh","ioe " );
			ioe.printStackTrace();
		}
		Log.d("saketh", "returning null");
		return null;
	}
	
	@Override
	protected void onPostExecute(Response result) {
		msg.obj = result;
		msg.sendToTarget();
	}

}
