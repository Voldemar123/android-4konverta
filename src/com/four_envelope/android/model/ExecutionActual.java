package com.four_envelope.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class ExecutionActual implements Serializable {

	private static final long serialVersionUID = -6852328163654598565L;

	@Attribute
	private String actualDate;

	@Attribute
	private String plannedDate;

	@Attribute
	private Integer defaultAccount;
	
	@Element
	private Float planned;
	
	@Element(required=false)
	private Float actual;
	
	@ElementList(inline=true,required=false)
	private ArrayList<ActualExpression> actualExpressions;
	

	public String getActualDate() {
		return actualDate;
	}
	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}
	public String getPlannedDate() {
		return plannedDate;
	}
	public void setPlannedDate(String plannedDate) {
		this.plannedDate = plannedDate;
	}
	public Float getPlanned() {
		return planned;
	}
	public void setPlanned(Float planned) {
		this.planned = planned;
	}
	public Float getActual() {
		return actual;
	}
	public void setActual(Float actual) {
		this.actual = actual;
	}
	public Integer getDefaultAccount() {
		return defaultAccount;
	}
	public void setDefaultAccount(Integer defaultAccount) {
		this.defaultAccount = defaultAccount;
	}
	public ArrayList<ActualExpression> getActualExpressions() {
		return actualExpressions;
	}
	public void setActualExpressions(ArrayList<ActualExpression> actualExpressions) {
		this.actualExpressions = actualExpressions;
	}
	
}
