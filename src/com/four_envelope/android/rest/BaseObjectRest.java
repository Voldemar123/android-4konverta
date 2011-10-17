package com.four_envelope.android.rest;

import java.io.Reader;
import java.io.StringReader;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import com.four_envelope.android.R;
import com.four_envelope.android.operation.LocalizedException;

/**
 * Base methods for object REST access
 * @author VMaximenko
 *
 */
public abstract class BaseObjectRest extends BaseRest {

	Serializer serializer;

	public BaseObjectRest() throws LocalizedException {
		super();		
		
		serializer = new Persister();
	}
	
	private <T> T XML2Object(Class<T> type, String xml) throws LocalizedException {
		if (xml == null || xml.length() == 0)
			return null;

		Reader reader = new StringReader( clearHTML(xml) );
		try {
			return serializer.read( type, reader, false );

		} catch (Exception e) {
			Log.i(getClass().getSimpleName(), e.getMessage());
			throw new LocalizedException( R.string.error_resource_access, e.getMessage() );
		}
	}
	
	public <T> T doGetObject(Class<T> type) throws LocalizedException {
		
		return XML2Object( 
				type, 
				doGet() );
	}

	public <T> T doPostObject(Class<T> type, String data) throws LocalizedException {

		return XML2Object( 
				type, 
				doPost(data) );
	}
	
	abstract public Object retrieve() throws LocalizedException;
	abstract public Object update(Object obj) throws LocalizedException;
}
