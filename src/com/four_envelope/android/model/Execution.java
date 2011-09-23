package com.four_envelope.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element(name="execution")
public class Execution implements Serializable {

	private static final long serialVersionUID = -780297272806792716L;

	@Attribute
	private String begin;

	@Attribute
	private Float remaining;

	@ElementList(inline=true,required=false)
	private ArrayList<ActualIncome> actualIncomes;

	@ElementList(inline=true,required=false)
	private ArrayList<ActualGoalCredit> actualGoalCredits;
	
	@ElementList(inline=true,required=false)
	private ArrayList<ActualExpense> actualExpenses;
	
	@Element
	private Envelope envelope;

	
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public Float getRemaining() {
		return remaining;
	}
	public void setRemaining(Float remaining) {
		this.remaining = remaining;
	}
	public ArrayList<ActualIncome> getActualIncomes() {
		return actualIncomes;
	}
	public void setActualIncomes(ArrayList<ActualIncome> actualIncomes) {
		this.actualIncomes = actualIncomes;
	}
	public ArrayList<ActualGoalCredit> getActualGoalCredits() {
		return actualGoalCredits;
	}
	public void setActualGoalCredits(ArrayList<ActualGoalCredit> actualGoalCredits) {
		this.actualGoalCredits = actualGoalCredits;
	}
	public ArrayList<ActualExpense> getActualExpenses() {
		return actualExpenses;
	}
	public void setActualExpenses(ArrayList<ActualExpense> actualExpenses) {
		this.actualExpenses = actualExpenses;
	}
	public Envelope getEnvelope() {
		return envelope;
	}
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}
	
}