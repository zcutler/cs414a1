package edu.colostate.cs.cs414.a1.zcutler;

import java.util.HashSet;
import java.util.Random;

public class Company {
	
	private String name;
	private HashSet<Worker> assignedWorkers = new HashSet<>();
	private HashSet<Worker> unassignedWorkers = new HashSet<>();
	private HashSet<Worker> availableWorkers = new HashSet<>();
	private HashSet<Project> projects = new HashSet<>();
	
	public Company(String companyName){
		if(companyName == null)
			throw new NullPointerException("Can not have a null Company name.");
		if(companyName.isEmpty())
			throw new RuntimeException("Missing a Company name.");
		this.name = companyName;
	}
	
	public HashSet<Worker> getAssignedWorkers() {
		return this.assignedWorkers;
	}
	
	public HashSet<Worker> getUnassignedWorkers() {
		return this.unassignedWorkers;
	}
	
	public String getName() {
		return name;
	}
	
	public HashSet<Worker> getAvailableWorkers(){
		return this.availableWorkers;
	}

	public HashSet<Project> getProjects() {
		return projects;
	}
	
	private int determineSalary(Worker worker) {
		Random rand = new Random();
		
		int salary = (rand.nextInt(50000) + 1) * worker.getQualifications().size();
		
		return salary;
	}
	
	private Worker getWorker(Worker worker) {
		
		if(!this.availableWorkers.contains(worker))
			throw new RuntimeException(worker.getName() + " is not an available worker.");
		
		for(Worker w : this.availableWorkers) {
			if(w.equals(worker)) {
				return w;
			}
		}
		return null;
	}
	
	public Project getProject(Project project) {
		
		if(!this.projects.contains(project))
			throw new RuntimeException(project.getName() + " is not a know project.");
		
		for(Project p : this.projects) {
			if(p.equals(project)) {
				return p;
			}
		}
		return null;
	}
		
	public void addToAvailableWorkerPool(Worker worker) {
		if(this.assignedWorkers.contains(worker) || this.availableWorkers.contains(worker))
			return;
		worker.setCompany(this);
		worker.setSalary(this.determineSalary(worker));
		this.availableWorkers.add(worker);
		this.unassignedWorkers.add(worker);
	}
	
	public void addToAvailableWorkerPool(HashSet<Worker> workers) {
		for(Worker worker : workers)
			this.addToAvailableWorkerPool(worker);
	}
	
	public void assign(Worker worker, Project project) {

	    if(worker == null || project == null)
	        throw new NullPointerException("Can not have null workers or projects.");

		if(!this.projects.contains(project))
			this.projects.add(project);
		
		Project currentProject = this.getProject(project);
		
		Worker currentWorker = this.getWorker(worker);
			
		currentProject.addWorker(currentWorker);
		
		currentWorker.addProjects(currentProject);
	
		if(!this.assignedWorkers.contains(currentWorker))
			this.assignedWorkers.add(worker);
		
		this.unassignedWorkers.remove(worker);
	}
	
	public void unassign(Worker worker, Project project) {

        if(worker == null || project == null)
            throw new NullPointerException("Can not have null workers or projects.");
		
		Project currentProject = this.getProject(project);
		
		Worker currentWorker = this.getWorker(worker);
		
		if(!this.assignedWorkers.contains(currentWorker)) 
		    return;
				
		currentProject.removeWorker(currentWorker);
				
		currentWorker.removeProjects(project);
		
		HashSet<Project> currentWorkersProjects = currentWorker.getProjects();
		if(currentWorkersProjects.size() == 0) {
			this.assignedWorkers.remove(currentWorker);
			this.unassignedWorkers.add(currentWorker);
		}
		
	}
	
	public void unassignAll(Worker worker) {

        if(worker == null)
            throw new NullPointerException("Can not have a null worker.");

		if(!this.assignedWorkers.contains(worker)) 
			return;
		
		Worker currentWorker = this.getWorker(worker);
		
		for(Project project : currentWorker.getProjects()) {
			this.unassign(currentWorker, project);
		}
	}
	
	public Project createProject(String name, HashSet<Qualification> qualifications, ProjectSize projectSize, ProjectStatus projectStatus) {
		projectStatus = ProjectStatus.PLANNED;
		Project project = new Project(name, projectSize, projectStatus);
		project.addQualifications(qualifications);
		
		if(this.projects.contains(project))
			throw new RuntimeException(project.getName() + " is already a known project.");
		
		this.projects.add(project);
		
		return project;
	}
	
	public void start(Project project) {
		Project currentProject = this.getProject(project);
		
		if(currentProject.getStatus() == ProjectStatus.ACTIVE)
			return;
		
		HashSet<Qualification> missingQualifications = currentProject.missingQualifications();
		if(missingQualifications.size() > 0)
			throw new RuntimeException(currentProject.getName() + " does not have all qualifications satisfied.");
		
		currentProject.setStatus(ProjectStatus.ACTIVE);
	}
	
	public void finish(Project project) {		
		Project currentProject = this.getProject(project);
		
		for(Worker worker : currentProject.getWorkers()) {
			this.unassign(worker, currentProject);
		}
		
		currentProject.setStatus(ProjectStatus.FINISHED);
	}
	
	@Override
	public String toString() {
		return this.name + ":" + String.valueOf(this.availableWorkers.size()) + ":" + String.valueOf(projects.size());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	public static void main(String[] args) {
		
		Qualification x = new Qualification("x");
		Qualification y = new Qualification("y");
		Qualification z = new Qualification("z");
		
		HashSet<Qualification> qualifications = new HashSet<Qualification>();
		qualifications.add(x);
		qualifications.add(y);
		qualifications.add(z);
		
		Worker zach = new Worker("zach", qualifications);
		Company exceptrinox = new Company("Exceptrinox");
		
		exceptrinox.addToAvailableWorkerPool(zach);
		exceptrinox.addToAvailableWorkerPool(zach);
		
	}

}
