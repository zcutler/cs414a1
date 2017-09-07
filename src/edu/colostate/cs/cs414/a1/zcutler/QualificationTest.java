package edu.colostate.cs.cs414.a1.zcutler;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class QualificationTest {


	private Qualification getQualificationWithMultipleWorkers() {
		Qualification x = new Qualification("x");

		HashSet<Qualification> qualifications = new HashSet<>();
		qualifications.add(x);

		Worker lyle = new Worker("chippah", qualifications);
		Worker paul = new Worker("uncle paul", qualifications);
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
		assertSame("x", x.toString());
	}

	@Test
	public void testToStringFail() {
		Qualification x = new Qualification("x");
		assertNotSame("xyz", x.toString());
	}

	@Test
	public void testAddToWorker() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		assertEquals(3, lyle.getQualifications().size());
	}

	@Test
	public void testAddToWorkerFailNullWorker() {
		Qualification z = new Qualification("z");
		try {
			z.addWorker(null);
			assertFalse(z.getWorkers().contains(null));
//			fail("Expected a NullPointerException to be thrown");
		}catch (NullPointerException e){
			String message = "Can not have a null worker.";
			assertEquals(message, e.getMessage());
		}
	}

	@Test
	public void testAddExistingQualificationToWorker() {
		Worker lyle = Worker.getWorkerWithQualifications("chippah");
		Qualification z = new Qualification("z");
		lyle.addQualification(z);
		assertEquals(3, lyle.getQualifications().size());
	}

	@Test
	public void testQualificationsWithMultipleWorkers() {
		Qualification x = getQualificationWithMultipleWorkers();
		assertEquals(3, x.getWorkers().size());
	}

	@Test
	public void testQualificationsContainsCorrectWorkers() {
		Qualification x = getQualificationWithMultipleWorkers();
		for(Worker worker : x.getWorkers())
			assertTrue(x.getWorkers().contains(worker));
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
		assertEquals(3, x.getWorkers().size());
	}

	@Test
	public void testNullDescription() {
		try{
			Qualification x = new Qualification(null);
			assertEquals(null, x.getDescription());
			//fail("Expected a NullPointerException to be thrown");
		}catch(Exception e){
			assertEquals("Can not have a null description.", e.getMessage());
		}
	}

	@Test
	public void testEmptyDescription() {
		try{
			Qualification x = new Qualification("");
			assertEquals("", x.getDescription());
//			fail("Expected a RuntimeException to be thrown");
		}catch(Exception e){
			assertEquals("Missing a description.", e.getMessage());
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
		assertEquals(2, x.getProjects().size());
	}

	@Test
	public void testQualificationContainsCorrectProjects() {
		Qualification x = getQualificationWithMultipleProjects();
		assertTrue(x.getProjects().contains(new Project("Dumbo Drop", ProjectSize.LARGE, ProjectStatus.ACTIVE)));
		assertTrue(x.getProjects().contains(new Project("Runway", ProjectSize.SMALL, ProjectStatus.PLANNED)));
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

	@Test
	public void testQualificationAddProjectsFailNull() {
		Qualification x = getQualificationWithMultipleProjects();
		try{
			x.addProject(null);
			assertFalse(x.getProjects().contains(null));
//			fail("Expected a NullPointerException to be thrown");
		}catch(Exception e){
			assertEquals("Can not have a null project.", e.getMessage());
		}
	}
}
