package edu.colostate.cs.cs414.a1.zcutler;

import java.util.HashSet;

public class Worker {
	
	private String nickName;
	private double salary;
	private HashSet<Qualification> qualifications = new HashSet<Qualification>(); 
	private HashSet<Project> projects = new HashSet<Project>(); 
	private Company company;
	private static final int workLoadThreshold = 12;
	private static final int baseSalary = 50000;

	public Worker(String name, HashSet<Qualification> qualifications){
		if(name == null)
			throw new NullPointerException("Can not have a null Worker name.");
		if(name.isEmpty())
			throw new RuntimeException("Missing a Worker name.");
		if(qualifications == null)
			throw new NullPointerException("Can not have null qualifications.");
		this.nickName = name;
		for(Qualification qualification : qualifications)
			this.addQualification(qualification);
		this.salary = 0.0d;
	}
	
	public HashSet<Qualification> getQualifications() {
		return qualifications;
	}
	
	public void addQualification(Qualification qualification) {
		if(qualification == null)
			throw new NullPointerException("Can not add a null qualification.");

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

		if(project == null)
			throw new NullPointerException("Can not have a null project.");

		if(this.projects.contains(project))
			return;
		
		if(this.willOverload(project))
			throw new RuntimeException(project.getName() + " will overload " + this.nickName + ".");
		
		this.projects.add(project);
		project.addWorker(this);
	}
	
	public void removeProjects(Project project) {
		if(project == null)
			throw new NullPointerException("Can not have a null project.");

		if(this.projects.contains(project)) {
			this.projects.remove(project);
			project.removeWorker(this);
		}
	}

	public boolean willOverload(Project project) {
		if(project == null)
			throw new NullPointerException("Can not have a null project.");

		boolean result = false;

		int numberOfLargeProjects = 0, numberOfMediumProjects = 0, numberOfSmallProjects = 0;
		
		if(project.getSize() == null) {
			throw new RuntimeException(project.getName() + " does not have a size.");
		}

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
		
		for(Project currentProject : this.projects) {
			if(currentProject.getStatus() == ProjectStatus.ACTIVE) {
				
				if(currentProject.getSize() == null) {
					throw new RuntimeException(currentProject.getName() + " does not have a size.");
				}

				switch(currentProject.getSize()) {
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
		}
		
		if(Worker.workLoadValue(numberOfLargeProjects, numberOfMediumProjects, numberOfSmallProjects) > workLoadThreshold)
			result = true;
		
		return result;
	}
	
	static private int workLoadValue(int numberOfLargeProjects, int numberOfMediumProjects, int numberOfSmallProjects) {
		return 3 * numberOfLargeProjects + 2 * numberOfMediumProjects + numberOfSmallProjects;
	}

	public void determineSalary() {
		this.salary =  baseSalary * qualifications.size();
	}

	static public Worker getWorkerWithQualifications(String name) {
		Qualification x = new Qualification("x");
		Qualification y = new Qualification("y");
		Qualification z = new Qualification("z");

		HashSet<Qualification> qualifications = new HashSet<>();
		qualifications.add(x);
		qualifications.add(y);
		qualifications.add(z);

		Worker lyle = new Worker(name, qualifications);

		lyle.determineSalary();

		return lyle;
	}

	static public Worker getWorkerWithQualificationsAndProjects(String name) {
		Worker lyle = Worker.getWorkerWithQualifications(name);
		Project runway = new Project("runway", ProjectSize.LARGE, ProjectStatus.SUSPENDED);
		Project runway2 = new Project("runway2", ProjectSize.LARGE, ProjectStatus.SUSPENDED);
		Project runway3 = new Project("runway3", ProjectSize.LARGE, ProjectStatus.SUSPENDED);
		Project runway4 = new Project("runway4", ProjectSize.LARGE, ProjectStatus.SUSPENDED);
		HashSet<Qualification> qualifications = lyle.getQualifications();
		runway.addQualifications(qualifications);
		runway2.addQualifications(qualifications);
		runway3.addQualifications(qualifications);
		runway4.addQualifications(qualifications);
		lyle.addProjects(runway);
		lyle.addProjects(runway2);
		lyle.addProjects(runway3);
		lyle.addProjects(runway4);

		for(Project projects : lyle.getProjects()){
			projects.setStatus(ProjectStatus.ACTIVE);
		}

		return lyle;
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
