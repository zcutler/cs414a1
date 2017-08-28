package edu.colostate.cs.cs414.a1.zcutler;

import java.util.HashSet;

public class Project {

	private String name;
	private ProjectSize projectSize; 
	private ProjectStatus projectStatus;
	private HashSet<Worker> workers = new HashSet<Worker>();
	private HashSet<Qualification> qualifications = new HashSet<Qualification>();
	
	public Project(String name, ProjectSize projectSize, ProjectStatus projectStatus) {
		this.name = name;
		this.projectSize = projectSize;
		this.projectStatus = projectStatus;
	}
	
	@Override
	public String toString() {
		return this.name + ":" + String.valueOf(this.workers.size()) + ":" + this.projectStatus.name();
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
		Project other = (Project) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public HashSet<Worker> getWorkers() {
		return workers;
	}

	public String getName() {
		return name;
	}

	public ProjectSize getSize() {
		return projectSize;
	}

	public ProjectStatus getStatus() {
		return projectStatus;
	}

	public void setStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}
	
	public void addWorker(Worker worker) {
		if(this.workers.contains(worker))
			return;
		
		if(!this.acceptingWorkers())
			throw new RuntimeException(this.name + " is not accepting workers. Project Status: " + this.projectStatus.name());
		
		if(!this.isHelpful(worker))
			throw new RuntimeException(worker.getName() + " will not be helpful on project " + this.name + ".");
		
		this.workers.add(worker);
		
		worker.addProjects(this);
	}
	
	public void removeWorker(Worker worker) {
		if(!workers.contains(worker)) 
			return;
		
		this.workers.remove(worker);
		
		HashSet<Qualification> missingQualifications = this.missingQualifications();
		if(missingQualifications.size() > 0 && this.projectStatus == ProjectStatus.ACTIVE)
			this.setStatus(ProjectStatus.SUSPENDED);
	}
	
	public void addWorkers(HashSet<Worker> workers) {
		for(Worker w : workers)
			this.addWorker(w);
	}
	
	public void addQualification(Qualification qualification) {
		if(!this.qualifications.contains(qualification)) {
			this.qualifications.add(qualification);
			qualification.addProject(this);
		}
			
	}
	
	public void addQualifications(HashSet<Qualification> qualifications) {
		for(Qualification qualification : qualifications) {
			this.addQualification(qualification);
		}
	}
	
	public boolean acceptingWorkers() {
		if(this.projectStatus != ProjectStatus.ACTIVE && this.projectStatus != ProjectStatus.FINISHED)
			return true;
		else
			return false;
	}
	
	public HashSet<Qualification> missingQualifications(){
		// set missingQualifications then we will remove all the qualifications that exist.
		HashSet<Qualification> missingQualifications = new HashSet<>(this.qualifications);
		
		HashSet<Qualification> presentQualifications = new HashSet<>();
		
		for(Worker worker : this.workers) {
			if(worker.getQualifications() != null) {
				HashSet<Qualification> workersQualifications = worker.getQualifications();
				presentQualifications.addAll(workersQualifications);
			}
		}
		
		for(Qualification qualification : missingQualifications) {
			if(presentQualifications.contains(qualification)) {
				missingQualifications.remove(qualification);
			}
		}
		
		return missingQualifications;
	}
	
	public boolean isHelpful(Worker worker) {
		boolean result = false;
		
		HashSet<Qualification> missingQualifications = this.missingQualifications();
		if(missingQualifications.size() == 0)
			return result;
		
		for(Qualification qualification : worker.getQualifications()) {
			if(missingQualifications.contains(qualification))
				return true;
		}
		
		return result;
	}

	public static void main(String[] args) {
		

	}

}