package edu.colostate.cs.cs414.a1.zcutler;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by zcutler on 8/30/2017.
 */
public class CompanyTest {

    @Test
    public void testNullName() {
        try{
            Company z = new Company(null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals(e.getMessage(), "Can not have a null Company name.");
        }
    }

    @Test
    public void testEmptyName() {
        try{
            Company z = new Company("");
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            assertEquals(e.getMessage(), "Missing a Company name.");
        }
    }

    @Test
    public void testGetAvailableWorkers() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);

        assertTrue(z.getAvailableWorkers().contains(lyle));
        assertTrue(z.getAvailableWorkers().contains(chip));
    }

    @Test
    public void testGetAvailableWorkersEmpty() {
        Company z = new Company("z");
        assertEquals(z.getAvailableWorkers().size(), 0);
    }

    @Test
    public void testGetUnassignedWorkers() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);

        assertTrue(z.getUnassignedWorkers().contains(lyle));
        assertTrue(z.getUnassignedWorkers().contains(chip));
    }

    @Test
    public void testAddWorkersSymmetry() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);

        for(Worker worker : z.getAvailableWorkers()){
            assertEquals(worker.getCompany(), z);
        }
    }

    @Test
    public void testEquals() {
        Company z = new Company("z");
        Company y = new Company("z");
        assertEquals(z,y);
    }

    @Test
    public void testNotEquals() {
        Company z = new Company("z");
        Company y = new Company("y");
        assertNotEquals(z,y);
    }

    @Test
    public void testToString() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);
        Project dumboDrop = Project.getProjectWithWorker("dumbo drop", lyle);
        Qualification magic = new Qualification("casting spells");
        chip.addQualification(magic);
        dumboDrop.addQualification(magic);
        dumboDrop.addWorker(chip);
        z.createProject("dumbo drop", dumboDrop.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        assertEquals(z.toString(), "z:2:1");
    }

    @Test
    public void testNotInUnassignedWorkers() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = Project.getProjectWithWorker("dumbo drop", lyle);
        Qualification magic = new Qualification("casting spells");
        dumboDrop.addQualification(magic);
        z.createProject("dumbo drop", dumboDrop.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        assertFalse(z.getUnassignedWorkers().contains(lyle));
    }

    @Test
    public void testAddWorkersFailAlreadyThere() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(lyle);
        assertEquals(z.getAvailableWorkers().size(), 1);
    }

    @Test
    public void testGetAssignedWorkers() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);
        Project dumboDrop = Project.getProjectWithWorker("dumbo drop", lyle);
        Qualification magic = new Qualification("casting spells");
        chip.addQualification(magic);
        dumboDrop.addQualification(magic);
        z.createProject("dumbo drop", dumboDrop.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(chip, dumboDrop);
        assertTrue(z.getAssignedWorkers().contains(lyle));
        assertTrue(z.getAssignedWorkers().contains(chip));
    }

    @Test
    public void testAssignFailNotAvailable() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = Project.getProjectWithWorker("dumbo drop", lyle);
        Qualification magic = new Qualification("casting spells");
        chip.addQualification(magic);
        dumboDrop.addQualification(magic);
        z.createProject("dumbo drop", dumboDrop.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        try {
            z.assign(chip, dumboDrop);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            assertEquals(e.getMessage(), "chip is not an available worker.");
        }
    }

    @Test
    public void testAssignFailAlreadyOnProject() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = Project.getProjectWithWorker("dumbo drop", lyle);
        z.createProject("dumbo drop", dumboDrop.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop);
        assertEquals(z.getProject(dumboDrop).getWorkers().size(), 1);
    }

    @Test
    public void testAssignFailProjectActive() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);
        Project dumboDrop = Project.getProjectWithWorker("dumbo drop", lyle);
        Qualification magic = new Qualification("casting spells");
        chip.addQualification(magic);
        dumboDrop.addQualification(magic);
        z.createProject("dumbo drop", dumboDrop.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(chip, dumboDrop);
        z.start(dumboDrop);
        try {
            z.assign(lyle, dumboDrop);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "dumbo drop is not accepting workers. Project Status: ACTIVE";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testAssignWorkerOverloaded() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway2 = new Project("runway2", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway3 = new Project("runway3", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway4 = new Project("runway4", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway5 = new Project("runway5", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("runway", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("runway2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("runway3", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("runway4", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("runway5", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, runway2);
        z.assign(lyle, runway3);
        z.assign(lyle, runway4);
        z.start(dumboDrop);
        z.start(runway2);
        z.start(runway3);
        z.start(runway4);
        try {
            z.assign(lyle, runway5);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "chippah will be overloaded if assigned to project runway5.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testAssignWorkerNowAddedToProject() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        assertTrue(z.getProject(dumboDrop).getWorkers().contains(lyle));
    }

    @Test
    public void testAssignLessMissingQualifications() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        int originalMissingQualifications = z.getProject(dumboDrop).missingQualifications().size();
        z.assign(lyle, dumboDrop);
        assertTrue(originalMissingQualifications > z.getProject(dumboDrop).missingQualifications().size());
    }

    @Test
    public void testAssignFailWorkerNotHelpful() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Qualification magic = new Qualification("casting spells");
        HashSet<Qualification> qualifications = new HashSet<>();
        qualifications.add(magic);
        Worker chip = new Worker("chip", qualifications);
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        chip.addQualification(magic);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        try {
            z.assign(chip, dumboDrop);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "chip will not be helpful on project dumbo drop.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testAssignFailNullWorkerNullProject() {
        Company z = new Company("z");
        try {
            z.assign(null, null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            String message = "Can not have null workers or projects.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void tesUnssignFailNullWorkerNullProject() {
        Company z = new Company("z");
        try {
            z.unassign(null, null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            String message = "Can not have null workers or projects.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testUnassignFailWorkerNotAssigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        assertFalse(z.getProject(dumboDrop).getWorkers().contains(chip));
        z.unassign(chip, dumboDrop);
        assertFalse(z.getProject(dumboDrop).getWorkers().contains(chip));
        assertFalse(z.getAssignedWorkers().contains(chip));
    }

    @Test
    public void testUnassignFailWorkerNotOnProject() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 =  new Project("dumbo drop2", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(chip, dumboDrop2);
        assertFalse(z.getProject(dumboDrop).getWorkers().contains(chip));
        z.unassign(chip, dumboDrop);
        assertFalse(z.getProject(dumboDrop).getWorkers().contains(chip));
    }

    @Test
    public void testUnassignRemovedFromAssignedWorkers() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);
        Qualification magic = new Qualification("casting spells");
        chip.addQualification(magic);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", chip.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(chip, dumboDrop);
        z.unassign(chip, dumboDrop);
        assertFalse(z.getAssignedWorkers().contains(chip));
    }

    @Test
    public void testUnassignWorkerBackInUnassignedWorkers() {
        Company z = new Company("z");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(chip);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", chip.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(chip, dumboDrop);
        assertTrue(z.getAssignedWorkers().contains(chip));
        z.unassign(chip, dumboDrop);
        assertTrue(z.getUnassignedWorkers().contains(chip));
    }

    @Test
    public void testUnassignPlannedProjectKeepsStatus() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        assertSame(z.getProject(dumboDrop).getStatus(), ProjectStatus.PLANNED);
        z.unassign(lyle, dumboDrop);
        assertSame(z.getProject(dumboDrop).getStatus(), ProjectStatus.PLANNED);
    }

    @Test
    public void testUnassignChangeStatusToSuspended() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.start(dumboDrop);
        assertSame(z.getProject(dumboDrop).getStatus(), ProjectStatus.ACTIVE);
        z.unassign(lyle, dumboDrop);
        assertSame(z.getProject(dumboDrop).getStatus(), ProjectStatus.SUSPENDED);
    }


}
