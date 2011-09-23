package com.four_envelope.android.rest;

import java.io.IOException;
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
	
	public BaseRest() {
		targetHost = new HttpHost( Constants.REST_TARGET_DOMAIN, 80, "http" );
		client = new DefaultHttpClient();
	}

	void setUrlParam(String from, String to) {
		url = url.replace("{" + from + "}", URLEncoder.encode(to) );
	}
	
	private HttpRequestBase addAppRequestHeaders(HttpRequestBase request) {
		request.addHeader("4KApplication", Constants.REST_APP_NAME);
		request.addHeader("4KVersion", Constants.REST_API_VERSION);
		request.addHeader("4KAuth", StoreClient.getLogin());
		
		return request;
	}
	
    protected String doGet() throws Exception {
    	Log.i(getClass().getSimpleName(), "doGet");
    	Log.i(getClass().getSimpleName(), url);
    	
        HttpGet getRequest = new HttpGet(url);
        addAppRequestHeaders(getRequest);
 
        try {
 
            HttpResponse getResponse = client.execute(targetHost, getRequest);
            
            final int statusCode = getResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_UNAUTHORIZED)
            	throw new UnauthorizedClientException();
            	
            if (statusCode != HttpStatus.SC_OK)
            	throw new RestClientException();
 
            HttpEntity getResponseEntity = getResponse.getEntity();
            if (getResponseEntity != null)
                return EntityUtils.toString(getResponseEntity);
 
        }
        catch (IOException e) {
        	throw new RestClientException(e);
        }
        finally {
        	getRequest.abort();
        }
        return null;
    }    
	
	
    protected String doPost(String content) throws Exception {
		Log.i(getClass().getSimpleName(), "doPost");
		Log.i(getClass().getSimpleName(), url);
		Log.i(getClass().getSimpleName(), content);

		HttpPost postRequest = new HttpPost(url);
		addAppRequestHeaders(postRequest);

		postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");

		try {
			StringEntity entity = new StringEntity(content, "UTF-8");
			postRequest.setEntity(entity);

			HttpResponse postResponse = client.execute(targetHost, postRequest);

			final int statusCode = postResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK)
				throw new RestClientException();

			HttpEntity postResponseEntity = postResponse.getEntity();
            if (postResponseEntity != null)
                return EntityUtils.toString(postResponseEntity);			

		} catch (IOException e) {
			throw new RestClientException(e);
		}
		finally {
			postRequest.abort();
        }
		return null;
	}
}
