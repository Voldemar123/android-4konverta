package com.four_envelope.android.model;

import java.io.Serializable;
import org.simpleframework.xml.Element;

@Element(name="actualExpense")
public class ActualExpense extends ExecutionActual implements Serializable {

	private static final long serialVersionUID = 7153956542471107556L;

	@Element
	private Expense expense;


	public Expense getExpense() {
		return expense;
	}
	public void setExpense(Expense expense) {
		this.expense = expense;
	}
	
}