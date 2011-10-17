package com.four_envelope.android.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.four_envelope.android.Constants;
import com.four_envelope.android.R;
import com.four_envelope.android.operation.LocalizedException;
import com.four_envelope.android.store.StoreClient;

import android.util.Log;

/**
 * Base methods for REST access to 4konverta.com API
 * @author VMaximenko
 */
public class BaseRest {

	private HttpHost targetHost;		
	private DefaultHttpClient client;
	 
	String url;
	private final String host = "http://" + Constants.REST_TARGET_DOMAIN;
	
	public BaseRest() throws LocalizedException {
		if ( !StoreClient.isLogged() )
			throw new LocalizedException(R.string.error_unauthorized_client);
		
		targetHost = new HttpHost( Constants.REST_TARGET_DOMAIN, 80, "http" );
		client = new DefaultHttpClient();
	}

	void setUrlParam(String from, String to) {
		url = url.replace("{" + from + "}", URLEncoder.encode(to) );
	}

	protected String clearHTML(String xml) {
		return xml.replaceAll("\\<span.*?\\>", "").replaceAll("\\</span\\>", "");
	}
	
	private HttpRequestBase addAppRequestHeaders(HttpRequestBase request) {
		request.addHeader("4KApplication", Constants.REST_APP_NAME);
		request.addHeader("4KVersion", Constants.REST_API_VERSION);
		request.addHeader("4KAuth", StoreClient.getPassword());
		
		return request;
	}
	
	private HttpURLConnection addAppRequestHeaders(HttpURLConnection conn) {
		conn.setRequestProperty("4KApplication", Constants.REST_APP_NAME);
		conn.setRequestProperty("4KVersion", Constants.REST_API_VERSION);
		conn.setRequestProperty("4KAuth", StoreClient.getPassword());
		
		return conn;
	}
	
    protected String doGet2() throws LocalizedException {
		Log.i(getClass().getSimpleName(), "get " + url);
    	
		HttpURLConnection conn = null;
        try {
			URL restUrl = new URL( host + url );
			conn = (HttpURLConnection) restUrl.openConnection();
			
			addAppRequestHeaders(conn);
			conn.setRequestMethod("GET");

			if ( conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
				throw new LocalizedException(R.string.error_unauthorized_client);
			
			if ( conn.getResponseCode() != HttpURLConnection.HTTP_OK)
				throw new LocalizedException(R.string.error_rest_client_status);
			
			BufferedReader rd = new BufferedReader(
					new InputStreamReader( conn.getInputStream() ) );

			StringBuilder sb = new StringBuilder();
			String line;
			while ( ( line = rd.readLine() ) != null )
				sb.append(line);

			rd.close();

			return sb.toString();				  

        }
        catch (IOException e) {
        	Log.e( getClass().getSimpleName(), e.getMessage() );
        	throw new LocalizedException( R.string.error_rest_client, e.getMessage() );
        }
        finally {
        	if ( conn != null )
        		conn.disconnect();
        }
    }	
	
    protected String doGet() throws LocalizedException {
		Log.i(getClass().getSimpleName(), "get " + url);
    	
        HttpGet getRequest = new HttpGet(url);
        addAppRequestHeaders(getRequest);
 
        try {
             HttpResponse getResponse = client.execute(targetHost, getRequest);
            
            final int statusCode = getResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_UNAUTHORIZED)
            	throw new LocalizedException( R.string.error_unauthorized_client );
            	
            if (statusCode != HttpStatus.SC_OK)
            	throw new LocalizedException( R.string.error_rest_client_status );
 
            HttpEntity getResponseEntity = getResponse.getEntity();
            if (getResponseEntity != null)
                return EntityUtils.toString(getResponseEntity);
 
        }
        catch (IOException e) {
        	throw new LocalizedException( R.string.error_rest_client, e.getMessage() );
        }
        finally {
        	getRequest.abort();
        }
        return null;
    }    
	
	
    protected String doPost(String content) throws LocalizedException {
		Log.i(getClass().getSimpleName(), "post " + url);

		HttpPost postRequest = new HttpPost(url);
		addAppRequestHeaders(postRequest);

		postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");

		try {
			StringEntity entity = new StringEntity(content, "UTF-8");
			postRequest.setEntity(entity);

			HttpResponse postResponse = client.execute(targetHost, postRequest);

			final int statusCode = postResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK)
				throw new LocalizedException( R.string.error_rest_client_status );

			HttpEntity postResponseEntity = postResponse.getEntity();
            if (postResponseEntity != null)
                return EntityUtils.toString(postResponseEntity);			

		} catch (IOException e) {
			throw new LocalizedException( R.string.error_rest_client, e.getMessage() );
		}
		finally {
			postRequest.abort();
        }
		return null;
	}
}
