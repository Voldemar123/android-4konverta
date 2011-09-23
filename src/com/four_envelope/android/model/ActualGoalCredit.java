package com.four_envelope.android.model;

import java.io.Serializable;
import org.simpleframework.xml.Element;

@Element(name="actualGoalCredit")
public class ActualGoalCredit extends ExecutionActual implements Serializable {

	private static final long serialVersionUID = 739618576663725875L;

	@Element
	private Goal goal;

	
	public Goal getGoal() {
		return goal;
	}
	public void setGoal(Goal goal) {
		this.goal = goal;
	}
	
}