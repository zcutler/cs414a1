package edu.colostate.cs.cs414.a1.zcutler;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class ProjectTest {

    @Test
    public void testNullName() {
        try{
            Project dumboDrop = new Project(null, ProjectSize.LARGE, ProjectStatus.FINISHED);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals(e.getMessage(), "Can not have a null Project name.");
        }
    }

    @Test
    public void testEmptyName() {
        try{
            Project dumboDrop = new Project("", ProjectSize.LARGE, ProjectStatus.FINISHED);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            assertEquals(e.getMessage(), "Missing a Project name.");
        }
    }

    @Test
    public void testToString() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        assertEquals(dumboDrop.toString(), "Dumbo Drop:1:SUSPENDED");
    }

    @Test
    public void testEquals() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        Project dumboDrop2 = Project.getProjectWithWorker("Dumbo Drop", chip);
        assertEquals(dumboDrop, dumboDrop2);
    }

    @Test
    public void testNotEquals() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        Project runway = new Project("runway", ProjectSize.LARGE, ProjectStatus.PLANNED);
        runway.addQualifications(chip.getQualifications());
        runway.addWorker(chip);
        assertNotEquals(dumboDrop, runway);
    }

    @Test
    public void testAddWorkerFailStatusFinished() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");

        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        dumboDrop.setStatus(ProjectStatus.FINISHED);

        try{
            dumboDrop.addWorker(chip);
            fail("Expected a RuntimeException to be thrown");
        } catch (RuntimeException e){
            String message = "Dumbo Drop is not accepting workers. Project Status: FINISHED";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testAddWorkerFailIsHelpful() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");

        Project dumboDrop = new Project("Dumbo Drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Qualification abc = new Qualification("abc");
        dumboDrop.addQualification(abc);

        try{
            dumboDrop.addWorker(lyle);
            fail("Expected a RuntimeException to be thrown");
        } catch (RuntimeException e){
            String message = "chippah will not be helpful on project Dumbo Drop.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testAddWorkerSuccess() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        assertTrue(dumboDrop.getWorkers().contains(lyle));
    }

    @Test
    public void testAddWorkerSuccessSymmetry() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        assertTrue(lyle.getProjects().contains(dumboDrop));
    }

    @Test
    public void testRemoveWorker() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        dumboDrop.removeWorker(lyle);
        assertFalse(dumboDrop.getWorkers().contains(lyle));
    }

    @Test
    public void testRemoveWorkerCorrectStatusSuspend() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        dumboDrop.setStatus(ProjectStatus.ACTIVE);
        assertSame(dumboDrop.getStatus(), ProjectStatus.ACTIVE);
        dumboDrop.removeWorker(lyle);
        assertSame(dumboDrop.getStatus(), ProjectStatus.SUSPENDED);
    }

    @Test
    public void testRemoveWorkerCorrectStatusStillActive() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        Qualification abc = new Qualification("abc");
        dumboDrop.addQualification(abc);
        Worker chip = Worker.getWorkerWithQualifications("chip");
        chip.addQualification(abc);
        dumboDrop.addWorker(chip);
        dumboDrop.setStatus(ProjectStatus.ACTIVE);
        dumboDrop.removeWorker(lyle);
        assertSame(dumboDrop.getStatus(), ProjectStatus.ACTIVE);
    }

    @Test
    public void testAddQualification() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        Qualification abc = new Qualification("abc");
        dumboDrop.addQualification(abc);
        assertTrue(dumboDrop.getQualifications().contains(abc));
    }

    @Test
    public void testSetActiveStatusFailMissingQualification() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        Qualification abc = new Qualification("abc");
        dumboDrop.addQualification(abc);

        try{
            dumboDrop.setStatus(ProjectStatus.ACTIVE);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            assertEquals(e.getMessage(), "Dumbo Drop still has missing qualifications.");
        }
    }

    @Test
    public void testMissingQualification() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        Qualification abc = new Qualification("abc");
        dumboDrop.addQualification(abc);
        assertFalse(dumboDrop.missingQualifications().isEmpty());
    }

    @Test
    public void testNoMissingQualification() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        assertTrue(dumboDrop.missingQualifications().isEmpty());
    }

    @Test
    public void testIsHelpFul() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        Qualification abc = new Qualification("abc");
        HashSet<Qualification> qualifications = new HashSet<>();
        qualifications.add(abc);
        dumboDrop.addQualification(abc);
        Worker paul = new Worker("Uncle Paul", qualifications);
        assertTrue(dumboDrop.isHelpful(paul));
    }

    @Test
    public void testIsNotHelpFul() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        Qualification abc = new Qualification("abc");
        Qualification def = new Qualification("def");
        HashSet<Qualification> qualifications = new HashSet<>();
        qualifications.add(abc);
        dumboDrop.addQualification(def);
        Worker paul = new Worker("Uncle Paul", qualifications);
        assertFalse(dumboDrop.isHelpful(paul));
    }
}
