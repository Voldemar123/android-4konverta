package com.four_envelope.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element(name="dailyExpense")
public class DailyExpense implements Serializable {

	private static final long serialVersionUID = 3506529035672053726L;

	@Attribute
	private String date;

	@Attribute
	private Integer defaultAccount;

	@Element
	private Float sum;
	
	@ElementList(inline=true,required=false)
	private ArrayList<Expression> expressions;


	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getDefaultAccount() {
		return defaultAccount;
	}
	public void setDefaultAccount(Integer defaultAccount) {
		this.defaultAccount = defaultAccount;
	}
	public Float getSum() {
		return sum;
	}
	public void setSum(Float sum) {
		this.sum = sum;
	}
	public ArrayList<Expression> getExpressions() {
		return expressions;
	}
	public void setExpressions(ArrayList<Expression> expressions) {
		this.expressions = expressions;
	}
	
}