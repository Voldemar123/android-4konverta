package com.four_envelope.android.model;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Text;

@Element(name="expression")
public class Expression implements Serializable {

	private static final long serialVersionUID = 5351671240708158041L;

	@Attribute
	private Integer account;

	@Attribute
	private String currency;

	@Text
	private String value;

	
	public Integer getAccount() {
		return account;
	}
	public void setAccount(Integer account) {
		this.account = account;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}