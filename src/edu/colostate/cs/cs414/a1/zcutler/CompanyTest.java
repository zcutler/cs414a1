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
            String nullString = null;
            Company z = new Company(nullString);
            assertNull(z.getName());
//            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            assertEquals("Can not have a null Company name.", e.getMessage());
        }
    }

    @Test
    public void testEmptyName() {
        try{
            Company z = new Company("");
            assertEquals("", z.getName());
//            fail("Expected a RuntimeException to be thrown");
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
            assertFalse(z.getProject(dumboDrop).getWorkers().contains(chip));
//            fail("Expected a RuntimeException to be thrown");
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
        assertEquals(1, z.getProject(dumboDrop).getWorkers().size());
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
            assertFalse(z.getProject(dumboDrop).getWorkers().contains(lyle));
//            fail("Expected a RuntimeException to be thrown");
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
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway = z.createProject("runway", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway2 = z.createProject("runway2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway3 = z.createProject("runway3", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway4 = z.createProject("runway4", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, runway);
        z.assign(lyle, runway2);
        z.assign(lyle, runway3);
        z.start(dumboDrop);
        z.start(runway);
        z.start(runway2);
        z.start(runway3);
        try {
            z.assign(lyle, runway4);
            assertFalse(z.getProject(runway4).getWorkers().contains(lyle));
//            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "chippah will be overloaded if assigned to project runway4.";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testStartFailWorkerOverloaded() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway = z.createProject("runway", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway2 = z.createProject("runway2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway3 = z.createProject("runway3", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project runway4 = z.createProject("runway4", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, runway);
        z.assign(lyle, runway2);
        z.assign(lyle, runway3);
        z.assign(lyle, runway4);
        z.start(dumboDrop);
        z.start(runway);
        z.start(runway2);
        z.start(runway3);
        try {
            z.start(runway4);
            assertNotEquals(ProjectStatus.ACTIVE, z.getProject(runway4).getStatus());
//            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "chippah will be overloaded if runway4 is started.";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testAssignWorkerNowAddedToProject() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        assertTrue(z.getProject(dumboDrop).getWorkers().contains(lyle));
    }

    @Test
    public void testAssignLessMissingQualifications() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
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
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        chip.addQualification(magic);
        try {
            z.assign(chip, dumboDrop);
            assertFalse(z.getProject(dumboDrop).getWorkers().contains(chip));
//            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "chip will not be helpful on project dumbo drop.";
            assertEquals(message, e.getMessage());
        }
    }

//    @Test
//    public void testAssignFailNullWorkerNullProject() {
//        Company z = new Company("z");
//        try {
//            z.assign(null, null);
//            fail("Expected a NullPointerException to be thrown");
//        }catch (NullPointerException e){
//            String message = "Can not have null workers or projects.";
//            assertEquals(message, e.getMessage());
//        }
//    }

//    @Test
//    public void tesUnssignFailNullWorkerNullProject() {
//        Company z = new Company("z");
//        try {
//            z.unassign(null, null);
//            fail("Expected a NullPointerException to be thrown");
//        }catch (NullPointerException e){
//            String message = "Can not have null workers or projects.";
//            assertEquals(message, e.getMessage());
//        }
//    }

    @Test
    public void testUnassignFailWorkerNotAssigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        z.addToAvailableWorkerPool(chip);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
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
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 = z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
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
        Project dumboDrop = z.createProject("dumbo drop", chip.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
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
        Project dumboDrop = z.createProject("dumbo drop", chip.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
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
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        assertSame(ProjectStatus.PLANNED, z.getProject(dumboDrop).getStatus());
        z.unassign(lyle, dumboDrop);
        assertSame(ProjectStatus.PLANNED, z.getProject(dumboDrop).getStatus());
    }

    @Test
    public void testUnassignChangeStatusToSuspended() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.start(dumboDrop);
        assertSame(ProjectStatus.ACTIVE, z.getProject(dumboDrop).getStatus());
        z.unassign(lyle, dumboDrop);
        assertSame(ProjectStatus.SUSPENDED, z.getProject(dumboDrop).getStatus());
    }

    @Test
    public void testUnassignAllFialNullWorker() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.start(dumboDrop);
        try {
            z.unassignAll(null);
            for(Worker worker : z.getAssignedWorkers())
                assertTrue(worker.getProjects().size() > 0);
//            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            String message = "Can not have a null worker.";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testUnassignAllMultipleProjectsNotAssigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 = z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
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
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 = z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop2);
        z.start(dumboDrop);
        z.start(dumboDrop2);
        for(Project project : z.getProjects())
            assertEquals(ProjectStatus.ACTIVE, project.getStatus());
        z.unassignAll(lyle);
        for(Project project : z.getProjects())
            assertEquals(ProjectStatus.SUSPENDED, project.getStatus());
    }

    @Test
    public void testUnassignAllFailWorkerNotAssigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        assertFalse(z.getAssignedWorkers().contains(lyle));
        z.unassignAll(lyle);
        assertFalse(z.getAssignedWorkers().contains(lyle));
    }

    @Test
    public void testUnassignAllWorkerNowUnassigned() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 = z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop2);
        z.start(dumboDrop);
        z.start(dumboDrop2);
        assertFalse(z.getUnassignedWorkers().contains(lyle));
        z.unassignAll(lyle);
        assertTrue(z.getUnassignedWorkers().contains(lyle));
    }

//    @Test
//    public void testStartFailNullProject() {
//        Company z = new Company("z");
//        try {
//            z.start(null);
//            fail("Expected a NullPointerException to be thrown");
//        }catch (NullPointerException e){
//            String message = "Can not have a null project.";
//            assertEquals(message, e.getMessage());
//        }
//    }

    @Test
    public void testStartFailUnknownProject() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project unknown =  new Project("unknown", ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        try {
            z.start(unknown);
            assertFalse(z.getProjects().contains(unknown));
            assertNotEquals(ProjectStatus.ACTIVE, unknown.getStatus());
//            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "unknown is not a know project.";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testStartCorrectProjectStatus() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        Project dumboDrop2 = z.createProject("dumbo drop2", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop2);
        for(Project project : z.getProjects())
            assertEquals(ProjectStatus.PLANNED, project.getStatus());
        z.start(dumboDrop);
        z.start(dumboDrop2);
        for(Project project : z.getProjects())
            assertEquals(ProjectStatus.ACTIVE, project.getStatus());
    }

    @Test
    public void testStartFailMissingQualifications() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        HashSet<Qualification> qualifications = new HashSet<>(lyle.getQualifications());
        Qualification magic = new Qualification("magic stuff");
        qualifications.add(magic);
        Project dumboDrop = z.createProject("dumbo drop", qualifications, ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        try {
            z.start(dumboDrop);
            assertNotEquals(ProjectStatus.ACTIVE, z.getProject(dumboDrop).getStatus());
//            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "dumbo drop does not have all qualifications satisfied.";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testStartFailMissingQualificationsCorrectStatus() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        HashSet<Qualification> qualifications = new HashSet<>(lyle.getQualifications());
        Qualification magic = new Qualification("magic stuff");
        qualifications.add(magic);
        Project dumboDrop = z.createProject("dumbo drop", qualifications, ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        try {
            z.start(dumboDrop);
            assertNotEquals(ProjectStatus.ACTIVE, z.getProject(dumboDrop).getStatus());
            assertEquals(ProjectStatus.PLANNED, z.getProject(dumboDrop).getStatus());
//            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            assertEquals(ProjectStatus.PLANNED, z.getProject(dumboDrop).getStatus());
        }
    }

    @Test
    public void testFinishFailNullProject() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.addToAvailableWorkerPool(lyle);
        z.assign(lyle, dumboDrop);
        z.start(dumboDrop);
        try {
            z.finish(null);
            assertNotEquals(ProjectStatus.FINISHED, z.getProject(dumboDrop).getStatus());
//            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            String message = "Can not have a null project.";
            assertEquals(message, e.getMessage());
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
            assertNotEquals(ProjectStatus.FINISHED, unknown.getStatus());
            assertNull(z.getProject(unknown));
//            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "unknown is not a know project.";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testFinishFailNotActive() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        try {
            z.finish(dumboDrop);
            assertNotEquals(ProjectStatus.FINISHED, z.getProject(dumboDrop).getStatus());
//            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "dumbo drop is not in an ACTIVE state. Only ACTIVE projects can be marked as finished.";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testFinishCorrectProjectStatus() {
        Company z = new Company("z");
        Worker lyle = Worker.getWorkerWithQualifications("chippah");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.start(dumboDrop);
        z.finish(dumboDrop);
        assertEquals(ProjectStatus.FINISHED, z.getProject(dumboDrop).getStatus());
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
            Project nullProject = z.createProject(null, null, null, null);
            assertNull(nullProject);
            assertFalse(z.getProjects().contains(nullProject));
//            fail("Expected a NullPointerException to be thrown");
        }catch (NullPointerException e){
            String message = "Can not have null qualifications.";
            assertEquals(message, e.getMessage());
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
        Project dumboDrop = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        try {
            Project nullProject = z.createProject("dumbo drop", lyle.getQualifications(), ProjectSize.SMALL, ProjectStatus.PLANNED);
            assertNull(nullProject);
            assertEquals(ProjectSize.LARGE, z.getProject(dumboDrop).getSize());
//            fail("Expected a RuntimeException to be thrown");
        }catch (RuntimeException e){
            String message = "dumbo drop is already a known project.";
            assertEquals(message, e.getMessage());
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
