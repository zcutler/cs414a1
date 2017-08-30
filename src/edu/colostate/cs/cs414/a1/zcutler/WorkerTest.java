package edu.colostate.cs.cs414.a1.zcutler;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class WorkerTest {


	@Test
	public void testToString() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
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

	@Test
	public void testSameQualification() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		Qualification x = new Qualification("x");
		lyle.addQualification(x);
		assertEquals(lyle.getQualifications().size(), 3);
	}

	@Test
	public void testQualificationSymmetry() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		HashSet<Qualification> qualifications = lyle.getQualifications();
		for(Qualification qualification : qualifications){
			assertTrue(qualification.getWorkers().contains(lyle));
		}
	}

	@Test
	public void testEquals() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		Worker chip = Worker.getWorkerWithQualifications("chippah");
		assertEquals(lyle, chip);
	}

	@Test
	public void testNotEquals() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		Worker chip = Worker.getWorkerWithQualifications("chip");
		assertNotEquals(lyle, chip);
	}

	@Test
	public void testAddProject() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		Project runway = new Project("runway", ProjectSize.SMALL, ProjectStatus.SUSPENDED);
		HashSet<Qualification> qualifications = lyle.getQualifications();
		runway.addQualifications(qualifications);
		lyle.addProjects(runway);
		assertTrue(lyle.getProjects().contains(runway));
	}

	@Test
	public void testAddProjectTwice() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		Project runway = new Project("runway", ProjectSize.SMALL, ProjectStatus.SUSPENDED);
		HashSet<Qualification> qualifications = lyle.getQualifications();
		runway.addQualifications(qualifications);
		lyle.addProjects(runway);
		lyle.addProjects(runway);
		assertEquals(lyle.getProjects().size(), 1);
	}

	@Test
	public void testAddProjectSymmetry() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		Project runway = new Project("runway", ProjectSize.SMALL, ProjectStatus.SUSPENDED);
		HashSet<Qualification> qualifications = lyle.getQualifications();
		runway.addQualifications(qualifications);
		lyle.addProjects(runway);
		assertTrue(runway.getWorkers().contains(lyle));
	}

	@Test
	public void testNotOverload() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		Project runway = new Project("runway", ProjectSize.SMALL, ProjectStatus.SUSPENDED);
		HashSet<Qualification> qualifications = lyle.getQualifications();
		runway.addQualifications(qualifications);
		assertFalse(lyle.willOverload(runway));
	}

	@Test
	public void testWillOverload() {
		Worker lyle = Worker.getWorkerWithQualificationsAndProjects("chippah");
		HashSet<Qualification> qualifications = lyle.getQualifications();

		Project dumboDrop = new Project("dumbo drop", ProjectSize.SMALL, ProjectStatus.SUSPENDED);
		dumboDrop.addQualifications(qualifications);
		assertTrue(lyle.willOverload(dumboDrop));
	}

	@Test
	public void testAddProjectFailOverload() {
		Worker lyle = Worker.getWorkerWithQualificationsAndProjects("chippah");
		HashSet<Qualification> qualifications = lyle.getQualifications();

		Project dumboDrop = new Project("dumbo drop", ProjectSize.SMALL, ProjectStatus.SUSPENDED);
		dumboDrop.addQualifications(qualifications);

		try {
			lyle.addProjects(dumboDrop);
			fail("Expected a RuntimeException to be thrown");
		}catch (RuntimeException e){
			assertEquals(e.getMessage(), "dumbo drop will overload chippah.");
		}
	}

	@Test
	public void testRemoveProject() {
		Worker lyle = Worker.getWorkerWithQualificationsAndProjects("chippah");
		Project runway = new Project("runway", ProjectSize.LARGE, ProjectStatus.ACTIVE);
		lyle.removeProjects(runway);
		assertFalse(lyle.getProjects().contains(runway));
	}

	@Test
	public void testRemoveProjectSymmetry() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		Project runway = new Project("runway", ProjectSize.LARGE, ProjectStatus.SUSPENDED);
		HashSet<Qualification> qualifications = lyle.getQualifications();
		runway.addQualifications(qualifications);
		lyle.addProjects(runway);
		lyle.removeProjects(runway);
		assertFalse(runway.getWorkers().contains(lyle));
	}
}
