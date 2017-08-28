package edu.colostate.cs.cs414.a1.zcutler;

import java.util.HashSet;

public class Worker {
	
	private String nickName;
	private double salary;
	private HashSet<Qualification> qualifications = new HashSet<Qualification>(); 
	private HashSet<Project> projects = new HashSet<Project>(); 
	private Company company;
	static int workLoadThreshold = 12;

	public Worker(String name, HashSet<Qualification> qualifications){
		this.nickName = name;
		for(Qualification qualification : qualifications)
			this.addQualification(qualification);
		this.salary = 0.0d;
	}
	
	public HashSet<Qualification> getQualifications() {
		return qualifications;
	}
	
	public void addQualification(Qualification qualification) {
		if(!this.qualifications.contains(qualification)) {
			this.qualifications.add(qualification);
			qualification.addWorker(this);
		}
			
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
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
		Worker other = (Worker) obj;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.nickName + ":" + String.valueOf(this.projects.size()) + ":" + String.valueOf(this.qualifications.size())
				+ ":" + String.valueOf(this.salary);
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getName() {
		return nickName;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public HashSet<Project> getProjects() {
		return projects;
	}

	public void addProjects(Project project) {
		if(this.projects.contains(project))
			return;
		
		if(this.willOverload(project))
			throw new RuntimeException(project.getName() + " will overload " + this.nickName + ".");
		
		this.projects.add(project);
		project.addWorker(this);
	}
	
	public void removeProjects(Project project) {
		if(this.projects.contains(project)) {
			this.projects.remove(project);
			project.removeWorker(this);
		}
			
	}
	
	static private void determineProjectSize(Project project, int numberOfLargeProjects, int numberOfMediumProjects, int numberOfSmallProjects) {
		switch(project.getSize()) {
		case LARGE:
			numberOfLargeProjects++;
			break;
		case MEDIUM:
			numberOfMediumProjects++;
			break;
		case SMALL:
			numberOfSmallProjects++;
			break;
	}	
	}
	
	public boolean willOverload(Project project) {
		boolean result = false;

		int numberOfLargeProjects = 0, numberOfMediumProjects = 0, numberOfSmallProjects = 0;
		
		if(project.getSize() == null) {
			throw new RuntimeException(project.getName() + " does not have a size.");
		}
		
		Worker.determineProjectSize(project, numberOfLargeProjects, numberOfMediumProjects, numberOfSmallProjects);
		
		for(Project currentProject : this.projects) {
			if(currentProject.getStatus() == ProjectStatus.ACTIVE) {
				
				if(currentProject.getSize() == null) {
					throw new RuntimeException(currentProject.getName() + " does not have a size.");
				}
				
				Worker.determineProjectSize(currentProject, numberOfLargeProjects, numberOfMediumProjects, numberOfSmallProjects);
			}
		}
		
		if(Worker.workLoadValue(numberOfLargeProjects, numberOfMediumProjects, numberOfSmallProjects) > 12)
			result = true;
		
		return result;
	}
	
	static private int workLoadValue(int numberOfLargeProjects, int numberOfMediumProjects, int numberOfSmallProjects) {
		return 3 * numberOfLargeProjects + 2 * numberOfMediumProjects + numberOfSmallProjects;
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
		
		System.out.println(zach.toString());

	}

}
