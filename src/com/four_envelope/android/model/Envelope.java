package com.four_envelope.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element(name="envelope")
public class Envelope implements Serializable {

	private static final long serialVersionUID = -3851597775163525676L;

	@Attribute
	private String begin;

	@Attribute
	private Float size;
	
	@ElementList(inline=true)
	private ArrayList<Person> persons;
	
	
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public Float getSize() {
		return size;
	}
	public void setSize(Float size) {
		this.size = size;
	}
	public ArrayList<Person> getPersons() {
		return persons;
	}
	public void setPersons(ArrayList<Person> persons) {
		this.persons = persons;
	}
	
}