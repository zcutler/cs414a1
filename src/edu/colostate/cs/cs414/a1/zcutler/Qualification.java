package edu.colostate.cs.cs414.a1.zcutler;

import java.util.HashSet;

public class Qualification {
	
	private String description;
	private HashSet<Worker> workers = new HashSet<Worker>();
	private HashSet<Project> projects = new HashSet<Project>();
	
	public Qualification(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		Qualification other = (Qualification) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
	
	public void addWorker(Worker worker) {
		if(!this.workers.contains(worker)) {
			this.workers.add(worker);
			worker.addQualification(this);
		}
			
	}
	
	public void addProject(Project project) {
		if(!this.projects.contains(project)) {
			this.projects.add(project);
			project.addQualification(this);
		}
			
	}
	
	public HashSet<Worker> getWorkers(){
		return this.workers;
	}

	public static void main(String[] args) {
		

	}

}
