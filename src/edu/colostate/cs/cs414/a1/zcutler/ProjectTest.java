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
            assertEquals("Can not have a null Project name.", e.getMessage());
        }
    }

    @Test
    public void testNullStatus() {
        try{
            Project dumboDrop = new Project("dumbo drop", ProjectSize.LARGE, null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals("Can not have a null project status.", e.getMessage());
        }
    }

    @Test
    public void testNullSize() {
        try{
            Project dumboDrop = new Project("dumbo drop", null, ProjectStatus.FINISHED);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals("Can not have a null project size.", e.getMessage());
        }
    }

    @Test
    public void testEmptyName() {
        try{
            Project dumboDrop = new Project("", ProjectSize.LARGE, ProjectStatus.FINISHED);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            assertEquals("Missing a Project name.", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        assertEquals("Dumbo Drop:1:SUSPENDED", dumboDrop.toString());
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
            assertEquals(message, e.getMessage());
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
            assertEquals(message, e.getMessage());
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
    public void testAddWorkerFailNull() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        try{
            dumboDrop.addWorker(null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals("Can not have a null worker.", e.getMessage());
        }
    }

    @Test
    public void testRemoveWorkerFailNull() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        try{
            dumboDrop.removeWorker(null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals("Can not have a null worker.", e.getMessage());
        }
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
        assertSame(ProjectStatus.ACTIVE, dumboDrop.getStatus());
        dumboDrop.removeWorker(lyle);
        assertSame(ProjectStatus.SUSPENDED, dumboDrop.getStatus());
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
        assertSame(ProjectStatus.ACTIVE, dumboDrop.getStatus());
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
            assertEquals("Dumbo Drop still has missing qualifications.", e.getMessage());
        }
    }

    @Test
    public void testSetStatusFailNull() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        try{
            dumboDrop.setStatus(null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals("Can not have a null project status.", e.getMessage());
        }
    }

    @Test
    public void testAddQualificationFailNull() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        try{
            dumboDrop.addQualification(null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals("Can not have a null qualification.", e.getMessage());
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
    public void testIsHelpful() {
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
    public void testIsHelpfulFailNull() {
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = Project.getProjectWithWorker("Dumbo Drop", lyle);
        try{
            dumboDrop.isHelpful(null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals("Can not have a null worker.", e.getMessage());
        }
    }

    @Test
    public void testIsNotHelpful() {
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
