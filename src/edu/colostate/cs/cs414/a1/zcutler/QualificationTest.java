package edu.colostate.cs.cs414.a1.zcutler;

//import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashSet;

import static org.testng.AssertJUnit.*;

public class QualificationTest {

	@Test
	public void testToString() {
		Qualification x = new Qualification("x");
		assertSame(x.toString(), "x");
	}

	@Test
	public void testToStringFail() {
		Qualification x = new Qualification("x");
		assertNotSame(x.toString(), "xyz");
	}

	private Worker getWorkerWithQualifications() {
		Qualification x = new Qualification("x");
		Qualification y = new Qualification("y");
		Qualification z = new Qualification("z");

		HashSet<Qualification> qualifications = new HashSet<>();
		qualifications.add(x);
		qualifications.add(y);
		qualifications.add(z);

		Worker lyle = new Worker("chippah", qualifications);

		return lyle;
	}

	private Qualification getQualificationWithMultipleWorkers() {
		Qualification x = new Qualification("x");

		HashSet<Qualification> qualifications = new HashSet<>();
		qualifications.add(x);

		@SuppressWarnings("unused")
		Worker lyle = new Worker("chippah", qualifications);
		@SuppressWarnings("unused")
		Worker paul = new Worker("uncle paul", qualifications);
		@SuppressWarnings("unused")
		Worker Edger = new Worker("edger", qualifications);

		return x;
	}

	@Test
	public void testAddToWorker() {
		Worker lyle = this.getWorkerWithQualifications();
		assertEquals(lyle.getQualifications().size(), 3);
	}

	@Test
	public void testAddExistingQualificationToWorker() {
		Worker lyle = this.getWorkerWithQualifications();
		Qualification z = new Qualification("z");
		lyle.addQualification(z);
		assertEquals(lyle.getQualifications().size(), 3);
	}

	@Test
	public void testQualificationsWithMultipleWorkers() {
		Qualification x = getQualificationWithMultipleWorkers();
		assertEquals(x.getWorkers().size(), 3);
	}

	@Test
	public void testQualificationsWithDuplicateWorkers() {
		Qualification x = getQualificationWithMultipleWorkers();
		HashSet<Qualification> qualifications = new HashSet<>();
		qualifications.add(x);
		Worker lyle = new Worker("chippah", qualifications);
		x.addWorker(lyle);
		assertEquals(x.getWorkers().size(), 3);
	}

}
