package com.four_envelope.android.rest;

import java.io.Reader;
import java.io.StringReader;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Base methods for object REST access
 * @author VMaximenko
 *
 */
public abstract class BaseObjectRest extends BaseRest {

	Serializer serializer;

	public BaseObjectRest() {
		super();		
		
		serializer = new Persister();
	}
	
	private <T> T XML2Object(Class<T> type, String xml) throws Exception {
		if (xml == null || xml.length() == 0)
			return null;

		Reader reader = new StringReader(xml);
		try {
			return serializer.read( type, reader, false );
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceAccessException(e);
		}
		finally {
			if ( reader != null )
				reader.close();
		}
	}
	
	public <T> T doGetObject(Class<T> type) throws Exception {
		
		return XML2Object( 
				type, 
				doGet() );
	}

	public <T> T doPostObject(Class<T> type, String data) throws Exception {

		return XML2Object( 
				type, 
				doPost(data) );
	}
	
	abstract public Object retrieve() throws Exception;
	abstract public Object update(Object obj) throws Exception;
}
