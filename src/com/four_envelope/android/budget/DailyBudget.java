package com.four_envelope.android.budget;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.four_envelope.android.model.ActualExpense;
import com.four_envelope.android.model.ActualGoalCredit;
import com.four_envelope.android.model.ActualIncome;
import com.four_envelope.android.model.DailyExpense;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.model.ExecutionActual;
import com.four_envelope.android.model.Person;

/**
 * Budget for one day from weekly envelope
 * @author VMaximenko
 *
 */
public class DailyBudget {
	

	public String date;
	public Date executionDate;
	public String title;
	public Execution execution;
	public int[] envelopeTotalLeft = new int[7];
	
	public Float envelopeSpent;
	public Float todaySpent;
	public Float envelopeRemaining;
	
	public ArrayList<ActualIncome> actualIncomes = new ArrayList<ActualIncome>();
	public ArrayList<ActualExpense> actualExpenses = new ArrayList<ActualExpense>();
	public ArrayList<ActualGoalCredit> actualGoalCredits = new ArrayList<ActualGoalCredit>();
	

	public String getTitle() {
		return title;
	}
	
	public void processEnvelope() {
// count envelope spent
		envelopeSpent = 0f;
		todaySpent = 0f;

		int[] envelopeTotalSpent = new int[7];
		for (Person person : execution.getEnvelope().getPersons()) {
			
			int day = 0;
			for (Iterator<DailyExpense> iterator = person.getDailyExpenses().iterator(); iterator.hasNext();) {
				DailyExpense dailyExpense = iterator.next();
				
				envelopeTotalSpent[day] += dailyExpense.getSum();
				day++;
				
				envelopeSpent += dailyExpense.getSum();
				
// left only one day
//				if ( !dailyExpense.getDate().equals( date ) )
//					iterator.remove();
//				else
					
				if ( dailyExpense.getDate().equals( date ) )					
					todaySpent += dailyExpense.getSum();
			}
		}
		
// TODO count spend for each envelope day		
//		int days = 7;
//		int total = 0;
//		for (int i = 0; i < days; i++) {
//			envelopeTotalLeft[i] = Math.round( (envelope.getSize() - total) / days - envelopeTotalSpent[i] );
//
//			total += envelopeTotalSpent[i];
//			if ( envelopeTotalSpent[i] > 0)
//				days--;
//		}		
			
		envelopeRemaining = execution.getEnvelope().getSize() - envelopeSpent;
	}
	
	
	public ArrayList<ExecutionActual> getActualActivities() {
		ArrayList<ExecutionActual> activities = new ArrayList<ExecutionActual>();
		
		activities.addAll( actualIncomes );
		activities.addAll( actualExpenses );
		activities.addAll( actualGoalCredits );
		
		return activities;
	}
	
	public CharSequence getEnvelopeSize() {
		return BudgetWork.formatMoney( execution.getEnvelope().getSize() );
	}
	public CharSequence getEnvelopeSpent() {
		return BudgetWork.formatMoney( envelopeSpent );
	}
	public CharSequence getTodaySpent() {
		return BudgetWork.formatMoney( todaySpent );
	}
	public Float getEnvelopeRemaining() {
		return envelopeRemaining;
	}
	public CharSequence getEnvelopeRemainingText() {
		return BudgetWork.formatMoney( envelopeRemaining );
	}

	
	/**
	 * Check execution planned data (in the past)
	 * @param actual
	 * @return
	 */
	public static boolean isPastPlannedDate(ExecutionActual actual) {
		return !actual.getActualDate().equals( actual.getPlannedDate() );
	}
}
