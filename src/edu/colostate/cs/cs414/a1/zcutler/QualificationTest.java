package edu.colostate.cs.cs414.a1.zcutler;

//import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashSet;

import static org.testng.AssertJUnit.*;

public class QualificationTest {

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

	private Qualification getQualificationWithMultipleProjects() {
		Qualification x = new Qualification("x");

		HashSet<Qualification> qualifications = new HashSet<>();
		qualifications.add(x);

		Project dumboDrop = new Project("Dumbo Drop", ProjectSize.LARGE, ProjectStatus.ACTIVE);
		Project runway = new Project("Runway", ProjectSize.SMALL, ProjectStatus.PLANNED);

		x.addProject(dumboDrop);
		x.addProject(runway);

		return x;
	}

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
	public void testQualificationAddWorkersSymmetry() {
		Qualification x = getQualificationWithMultipleWorkers();
		HashSet<Worker> workers = x.getWorkers();
		for (Worker worker : workers) {
			assertTrue(worker.getQualifications().contains(x));
		}
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

	@Test
	public void testNullDescription() {
		try{
			Qualification x = new Qualification(null);
			fail("Expected a NullPointerException to be thrown");
		}catch(Exception e){
			assertEquals(e.getMessage(), "Can not have a null description.");
		}
	}

	@Test
	public void testEmptyDescription() {
		try{
			Qualification x = new Qualification("");
			fail("Expected a RuntimeException to be thrown");
		}catch(Exception e){
			assertEquals(e.getMessage(), "Missing a description.");
		}
	}

	@Test
	public void testEquals() {
		Qualification x = new Qualification("x");
		Qualification y = new Qualification("x");
		assertTrue(x.equals(y));
	}

	@Test
	public void testNotEquals() {
		Qualification x = new Qualification("x");
		Qualification y = new Qualification("y");
		assertFalse(x.equals(y));
	}

	@Test
	public void testQualificationWithMultipleProjects() {
		Qualification x = getQualificationWithMultipleProjects();
		assertEquals(x.getProjects().size(), 2);
	}

	@Test
	public void testQualificationAddProjects() {
		Qualification x = getQualificationWithMultipleProjects();
		HashSet<Project> p = x.getProjects();
		Project dumboDrop = new Project("Dumbo Drop", ProjectSize.LARGE, ProjectStatus.ACTIVE);
		Project runway = new Project("Runway", ProjectSize.SMALL, ProjectStatus.PLANNED);
		assertTrue(p.contains(dumboDrop));
		assertTrue(p.contains(runway));
	}

	@Test
	public void testQualificationAddProjectsFalse() {
		Qualification x = getQualificationWithMultipleProjects();
		HashSet<Project> p = x.getProjects();
		Project impossible = new Project("impossible", ProjectSize.MEDIUM, ProjectStatus.SUSPENDED);
		assertFalse(p.contains(impossible));
	}

	@Test
	public void testQualificationAddProjectsSymmetry() {
		Qualification x = getQualificationWithMultipleProjects();
		HashSet<Project> projects = x.getProjects();
		for (Project project :	projects) {
			assertTrue(project.getQualifications().contains(x));
		}
	}
}
