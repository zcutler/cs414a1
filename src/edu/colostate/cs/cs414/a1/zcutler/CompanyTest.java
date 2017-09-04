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
            assertEquals("Can not have a null Company name.", e.getMessage());
        }
    }

    @Test
    public void testEmptyName() {
        try{
            Company z = new Company("");
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            assertEquals("Missing a Company name.", e.getMessage());
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
        assertEquals(0, z.getAvailableWorkers().size());
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
            assertEquals(z, worker.getCompany());
        }
    }

    @Test
    public void testEquals() {
        Company z = new Company("z");
        Company y = new Company("z");
        assertEquals(z, y);
    }

    @Test
    public void testNotEquals() {
        Company z = new Company("z");
        Company y = new Company("y");
        assertNotEquals(z, y);
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
        assertEquals("z:2:1", z.toString());
    }

    @Test
    public void testNotInUnassignedWorkers() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Qualification magic = new Qualification("casting spells");
        dumboDrop.addQualification(magic);
        z.assign(lyle, dumboDrop);
        assertFalse(z.getUnassignedWorkers().contains(lyle));
    }

    @Test
    public void testAddWorkersFailAlreadyThere() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(lyle);
        assertEquals(1, z.getAvailableWorkers().size());
    }

    @Test
    public void testGetAssignedWorkers() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Qualification magic = new Qualification("casting spells");
        chip.addQualification(magic);
        dumboDrop.addQualification(magic);
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
        Project dumboDrop = z.createProject("dumbo drop", chip.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Qualification magic = new Qualification("casting spells");
        chip.addQualification(magic);
        dumboDrop.addQualification(magic);
        z.assign(lyle, dumboDrop);
        try {
            z.assign(chip, dumboDrop);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            assertEquals("chip is not an available worker.", e.getMessage());
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

    @Test
    public void testUnassignAllFialNullWorker() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.start(dumboDrop);
        try {
            z.unassignAll(null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            String message = "Can not have a null worker.";
            assertEquals(e.getMessage(), message);
        };
    }

    @Test
    public void testUnassignAllMultipleProjectsNotAssigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 =  new Project("dumbo drop2", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop2);
        z.start(dumboDrop);
        z.start(dumboDrop2);
        z.unassignAll(lyle);
        assertFalse(z.getAssignedWorkers().contains(lyle));
    }

    @Test
    public void testUnassignAllProjectNoLongerActive() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 =  new Project("dumbo drop2", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop2);
        z.start(dumboDrop);
        z.start(dumboDrop2);
        for(Project project : z.getProjects())
            assertEquals(project.getStatus(), ProjectStatus.ACTIVE);
        z.unassignAll(lyle);
        for(Project project : z.getProjects())
            assertEquals(project.getStatus(), ProjectStatus.SUSPENDED);
    }

    @Test
    public void testUnassignAllFailWorkerNotAssigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        assertFalse(z.getAssignedWorkers().contains(lyle));
        z.unassignAll(lyle);
        assertFalse(z.getAssignedWorkers().contains(lyle));
    }

    @Test
    public void testUnassignAllWorkerNowUnassigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 =  new Project("dumbo drop2", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop2);
        z.start(dumboDrop);
        z.start(dumboDrop2);
        assertFalse(z.getUnassignedWorkers().contains(lyle));
        z.unassignAll(lyle);
        assertTrue(z.getUnassignedWorkers().contains(lyle));
    }

    @Test
    public void testStartFailNullProject() {
        Company z = new Company("z");
        try {
            z.start(null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            String message = "Can not have a null project.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testStartFailUnknownProject() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project unknown =  new Project("unknown", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        try {
            z.start(unknown);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "unknown is not a know project.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testStartCorrectProjectStatus() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 =  new Project("dumbo drop2", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop2);
        for(Project project : z.getProjects())
            assertEquals(project.getStatus(), ProjectStatus.PLANNED);
        z.start(dumboDrop);
        z.start(dumboDrop2);
        for(Project project : z.getProjects())
            assertEquals(project.getStatus(), ProjectStatus.ACTIVE);
    }

    @Test
    public void testStartFailMissingQualifications() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        HashSet<Qualification> qualifications = new HashSet<>(lyle.getQualifications());
        Qualification magic = new Qualification("magic stuff");
        qualifications.add(magic);
        z.createProject("dumbo drop", qualifications, ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        try {
            z.start(dumboDrop);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "dumbo drop does not have all qualifications satisfied.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testStartFailMissingQualificationsCorrectStatus() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        HashSet<Qualification> qualifications = new HashSet<>(lyle.getQualifications());
        Qualification magic = new Qualification("magic stuff");
        qualifications.add(magic);
        z.createProject("dumbo drop", qualifications, ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        try {
            z.start(dumboDrop);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            assertEquals(z.getProject(dumboDrop).getStatus(), ProjectStatus.PLANNED);
        }
    }

    @Test
    public void testFinishFailNullProject() {
        Company z = new Company("z");
        try {
            z.finish(null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            String message = "Can not have a null project.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testFinishFailUnknownProject() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project unknown =  new Project("unknown", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        try {
            z.finish(unknown);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "unknown is not a know project.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testFinishFailNotActive() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        try {
            z.finish(dumboDrop);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "dumbo drop is not in an ACTIVE state. Only ACTIVE projects can be marked as finished.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testFinishCorrectProjectStatus() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  new Project("dumbo drop", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.start(dumboDrop);
        z.finish(dumboDrop);
        assertEquals(z.getProject(dumboDrop).getStatus(), ProjectStatus.FINISHED);
    }

    @Test
    public void testFinishWorkerNoLongerAssigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.start(dumboDrop);
        assertTrue(z.getAssignedWorkers().contains(lyle));
        z.finish(dumboDrop);
        assertFalse(z.getAssignedWorkers().contains(lyle));
    }

    @Test
    public void testFinishWorkerStillAssigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 = z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop2);
        z.start(dumboDrop);
        z.start(dumboDrop2);
        assertTrue(z.getAssignedWorkers().contains(lyle));
        z.finish(dumboDrop);
        assertTrue(z.getAssignedWorkers().contains(lyle));
    }

    @Test
    public void testFinishWorkerNoLongerInProject() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.start(dumboDrop);
        assertTrue(z.getProject(dumboDrop).getWorkers().contains(lyle));
        z.finish(dumboDrop);
        assertFalse(z.getProject(dumboDrop).getWorkers().contains(lyle));
    }

    @Test
    public void testCreateProjectFailNull() {
        Company z = new Company("z");
        try {
            z.createProject(null, null, null, null);
            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            String message = "Can not have null qualifications.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testCreateProjectSuccess() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 = z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        assertTrue(z.getProjects().contains(dumboDrop));
        assertTrue(z.getProjects().contains(dumboDrop2));
    }

    @Test
    public void testCreateProjectFailAlreadyExists() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        try {
            z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.SMALL, ProjectStatus.PLANNED);
            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "dumbo drop is already a known project.";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testCreateProjectCorrectStatusAndSize() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop =  z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        assertEquals(ProjectStatus.PLANNED, dumboDrop.getStatus());
        assertEquals(ProjectSize.LARGE, dumboDrop.getSize());
    }

}
