package projects.service;

import java.util.List;
import java.util.NoSuchElementException;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectService {

	private static ProjectDao projectDao = new ProjectDao();
	
	
	// addProject service layer method used to call the insertProject method from the DAO projectDao class
	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}

	// fetchAllProjects service layer method used to call the fetchAllProjects method from the DAO projectDao class
	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}

	// fetchProjectById service layer method used to call the fetchProjectById method from the DAO layer projectDao class
	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException("Project with ID=" + projectId + " does not exist!"));
	}

	public void modifyProjectDetails(Project project) {
		if (!projectDao.modifyProjectDetails(project)) {
			throw new DbException("Project with ID= " + project.getProjectId() + "does not exist");
		}
	}

	public void deleteProject(Integer projectId) {
		if(!projectDao.deleteProject(projectId)) {
			throw new DbException("Project ID " + projectId + "does not exist!");
		}
	}

	
}
