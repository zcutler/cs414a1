package edu.colostate.cs.cs414.a1.zcutler;

// Eclipse Block ~~~~~~~~~~~

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

// END Eclipse Block ~~~~~~~~~~~

// IntelliJ Block  ~~~~~~~~~~~~~~
//import org.junit.Test;
//
//import java.util.HashSet;
//
//import static org.testng.AssertJUnit.*;

// END IntelliJ Block  ~~~~~~~~~~~~~~

public class WorkerTest {


	@Test
	public void testToString() {
		Worker lyle = Worker.getWorkerWithQualifications();
		Project dumboDrop = new Project("Dumbo Drop", ProjectSize.LARGE, ProjectStatus.SUSPENDED);
		Project runway = new Project("Runway", ProjectSize.SMALL, ProjectStatus.PLANNED);
		HashSet<Qualification> qualifications = lyle.getQualifications();
		dumboDrop.addQualifications(qualifications);
		runway.addQualifications(qualifications);
		lyle.addProjects(dumboDrop);
		lyle.addProjects(runway);
		assertEquals(lyle.toString(), "chippah:2:3:150000.0");
	}

	@Test
	public void testNullQualifications() {
		try{
			Worker lyle = new Worker("chippah", null);
			fail("Expected a NullPointerException to be thrown");
		}catch(NullPointerException e){
			assertEquals(e.getMessage(), "Can not have null qualifications.");
		}
	}

	@Test
	public void testNullName() {
		try{
			Qualification x = new Qualification("x");
			HashSet<Qualification> qualifications = new HashSet<>();
			qualifications.add(x);
			Worker lyle = new Worker(null, qualifications);
			fail("Expected a NullPointerException to be thrown");
		}catch(NullPointerException e){
			assertEquals(e.getMessage(), "Can not have a null Worker name.");
		}
	}



}
