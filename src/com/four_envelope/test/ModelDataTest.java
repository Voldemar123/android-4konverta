package com.four_envelope.test;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.four_envelope.android.budget.BudgetWork;
import com.four_envelope.android.budget.DailyBudget;
import com.four_envelope.android.model.Envelope;
import com.four_envelope.android.model.Execution;
import com.four_envelope.android.model.User;

import junit.framework.TestCase;

public class ModelDataTest extends TestCase {

	private String dataFolder = "data/"; 
	private static final DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
	Serializer serializer;
	
	@Before
    public void setUp() {
		serializer = new Persister();
	}	
	
	@Test
    public void testUser() throws Exception {
		
		File source = new File(dataFolder + "user2.xml");

		User data = serializer.read(User.class, source);	
		
		assertEquals( "ua", data.getCountry().getCode() );
		assertEquals( "ua", data.getCurrency().getId() );
		assertEquals( new Integer(2), data.getFirstDayOfWeek() );

		assertTrue( data.getPersons().size() == 4 );
		assertEquals( data.getPersons().get(0).getId(), new Integer(19824747) );
		
		assertTrue( data.getAccounts().size() == 3 );
		
		BudgetWork.userData = data;
		
// look for envelope begin date
		String today = "2011-07-07";
		String weekBegin = "2011-07-04";
		
		assertEquals( weekBegin, BudgetWork.calcEnvelopeBegin( dfDate.parse(today) ) );
	}

	@Test
    public void testEnvelope() throws Exception {
		
		File source = new File(dataFolder + "envelope2.xml");

		Envelope data = serializer.read(Envelope.class, source);	
		
		assertEquals( "2011-05-30", data.getBegin() );

		assertTrue( data.getPersons().size() == 4 );
		assertTrue( data.getPersons().get(0).getDailyExpenses().size() == 7 );
	}

	@Test
    public void testExecution() throws Exception {
		
		File source = new File(dataFolder + "execution2.xml");

		Execution data = serializer.read(Execution.class, source);	
		
		assertEquals( "2011-09-12", data.getBegin() );
		assertEquals( "2011-09-12", data.getEnvelope().getBegin() );

		assertTrue( data.getActualExpenses().size() > 0 );
		assertTrue( data.getActualIncomes().size() > 0 );
//		assertTrue( data.getActualGoalCredits().size() > 0 );
		
		Date checkDate = dfDate.parse("2011-09-16");
		
		DailyBudget weeklyEnvelope = BudgetWork.prepareDailyBudget(checkDate, data);
		assertNotNull( weeklyEnvelope );
		assertTrue( weeklyEnvelope.actualExpenses.size() > 0 );
		assertTrue( weeklyEnvelope.actualIncomes.size() > 0 );
	}

}
