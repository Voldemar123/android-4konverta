package com.four_envelope.android.model;

import java.io.Serializable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

@Element(name="income")
public class Income implements Serializable {

	private static final long serialVersionUID = -7999963973000470085L;

	@Attribute
	private Integer id;

	@Attribute
	private String name;

	@Element
	private Currency currency;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}