package com.four_envelope.android.model;

import java.io.Serializable;
import org.simpleframework.xml.Element;

@Element(name="actualIncome")
public class ActualIncome extends ExecutionActual implements Serializable {

	private static final long serialVersionUID = 7153956542471107556L;

	@Element
	private Income income;

	public Income getIncome() {
		return income;
	}
	public void setIncome(Income income) {
		this.income = income;
	}
	
}