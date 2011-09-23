package com.four_envelope.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element(name="user")
public class User implements Serializable {

	private static final long serialVersionUID = -3774033844237118337L;

	@Element
	private Country country;
	
	@Element
	private Currency currency;

	@Element
	private Integer firstDayOfWeek;
	
	@Element
	private String timeZone;

	@Element
	private Boolean disableExtendedSyntax;
	
	@ElementList
	private ArrayList<Person> persons;

	@ElementList(required=false)
	private ArrayList<Account> accounts;

	
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public Integer getFirstDayOfWeek() {
		return firstDayOfWeek;
	}
	public void setFirstDayOfWeek(Integer firstDayOfWeek) {
		this.firstDayOfWeek = firstDayOfWeek;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public Boolean getDisableExtendedSyntax() {
		return disableExtendedSyntax;
	}
	public void setDisableExtendedSyntax(Boolean disableExtendedSyntax) {
		this.disableExtendedSyntax = disableExtendedSyntax;
	}
	public ArrayList<Person> getPersons() {
		return persons;
	}
	public void setPersons(ArrayList<Person> persons) {
		this.persons = persons;
	}
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}

}