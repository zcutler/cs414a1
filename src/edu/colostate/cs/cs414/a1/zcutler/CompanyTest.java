package edu.colostate.cs.cs414.a1.zcutler;

import org.junit.Test;

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
        Worker chip = Worker.getWorkerWithQualifications("chip");
        z.addToAvailableWorkerPool(lyle);
        Project dumboDrop = Project.getProjectWithWorker("dumbo drop", lyle);
        Qualification magic = new Qualification("casting spells");
        chip.addQualification(magic);
        dumboDrop.addQualification(magic);
        z.createProject("dumbo drop", dumboDrop.getQualifications(), ProjectSize.LARGE, ProjectStatus.PLANNED);
        z.assign(lyle, dumboDrop);
        z.assign(lyle, dumboDrop);
        assertEquals(z.getProject(dumboDrop).getWorkers().size(), 1);
    }
}
