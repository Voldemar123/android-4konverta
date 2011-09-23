package com.four_envelope.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element(name="person")
public class Person implements Serializable {
	
	private static final long serialVersionUID = 8906183284195280150L;

	@Attribute
	private Integer id;

	@Attribute
	private String name;

	@ElementList(inline=true,required=false)
	private ArrayList<DailyExpense> dailyExpenses;
	
	
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
	public ArrayList<DailyExpense> getDailyExpenses() {
		return dailyExpenses;
	}
	public void setDailyExpenses(ArrayList<DailyExpense> dailyExpenses) {
		this.dailyExpenses = dailyExpenses;
	}
	
}