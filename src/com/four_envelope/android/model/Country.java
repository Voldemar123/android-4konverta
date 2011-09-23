package com.four_envelope.android.model;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Text;

@Element(name="country")
public class Country implements Serializable {

	private static final long serialVersionUID = -7397120408863629989L;

	@Attribute
	private String code;

	@Text
	private String value;

	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
