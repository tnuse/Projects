package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	
	// List of operations / menu
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project",
			"2) List projects",
			"3) Select available project",
			"4) Update a Project",
			"5) Delete a Project"
			);
			// @formatter:on

	// Scanner for user input
	private Scanner scanner = new Scanner(System.in);
	
	// variable instance of type ProjectService named projectService calling on ProjectService class in
	// projects.service package
	private ProjectService projectService = new ProjectService();
	private Project curProject;
	
	
	
	
	public static void main(String[] args) {
	
	// Calling the processUserSelections Method to process the user selection.
		new ProjectsApp().processUserSelections();
		

	}

	// processUserSelections Method using a try/catch block for validation of user input and returning 
	// a message to try again if an invalid selection is made.  Also setting up a Switch inside of the try catch block
	// to call methods based on the user selection.
	
	
	private void processUserSelections() {
			boolean done = false;
		
		while (!done) {
			try {
				int selection = getUserSelection();
			
			switch (selection) {
			case -1:
				done = exitMenu();
				break;
			
			case 1:
				createProject();
				break;
				
			case 2:
				listProjects();
				break;
				
			case 3:
				selectProject();
				break;
				
			case 4:
				updateProjectDetails();
				break;
				
			case 5:
				deleteProject();
				break;
				
				
			default:
				System.out.println("\n" + selection + " is not a valid selection.  Try again.");
				break;
			}
			
			} catch (Exception e) {
			System.out.println("\nError: " + e + " Try again.");
		}
			
		}
	} // END of processUserSelections

	
	private void deleteProject() {
		listProjects();
		Integer projectId = getIntInput("Enter the Project ID you would like to delete: ");
		
			projectService.deleteProject(projectId);
			System.out.println("You have deleted project ID: " + projectId);
			
			if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
				curProject = null;
			}
		
	}

	private void updateProjectDetails() {
		if(Objects.isNull(curProject)) {
			System.out.println("\nPlease select a project first.");
			return;
		}
		
		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal actualHours = getDecimalInput("Enter the estimated hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter the difficulty of this project (1-5) [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter the notes for this project [" + curProject.getNotes() + "]");
		
		Project project = new Project();
		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		
		
		projectService.modifyProjectDetails(project);
		curProject = projectService.fetchProjectById(curProject.getProjectId());
			
		
		
	} // END of updateProjectDetails

	// selectProject Method to select a project to view
	private void selectProject() {
		listProjects();
		
		Integer projectId = getIntInput("Select a Project ID");
		
		curProject = null;
		
		curProject = projectService.fetchProjectById(projectId);
		
		if (Objects.isNull(curProject)) {
			System.out.println("\nInvalid project ID!");
		} else {
			System.out.println("You are working with Project ID: " + curProject);
		}
	} // END of selectProject method

	
	// listProjects Method to list out all available projects
	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects: ");
		
		// Lambda expression to print out each project_id and project_name in the project table
		projects.forEach(project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));
		
	} // END of listProjects method

	
	// printOperations method to print out selection menu
	private void printOperations() {
		System.out.println("\nThere are the available selections.  (Press enter key to quit):");
		
		operations.forEach(line -> System.out.println(" " + line));
	} // END of printOperations
	
	
	// getUserSelection method to get the user selection from the menu.
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter menu selection: ");
		
		return Objects.isNull(input) ? -1 : input;
		
	} // END of getUserSelection Method
	

	// getStringInput method for the notes and project_name columns in the project table.
	
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		return input.isBlank() ? null : input.trim();
	
	} // END of get StringInput method

	
	// getInInput method to attempt to convert scanner String input to an Integer for the difficulty column in the project table.
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if (Objects.isNull(input)) {
			return null;
		}
		
		try {
			return Integer.valueOf(input);
			
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid selection.");
			
		}
		
	} // END of getInInput
	
	
	// getBigDecimal method to convert the BigDecimal value of prompt to a number with 2 digits to the right of the decimal 
	// place, conforming it to the Decimal requirements in the estimated_hours and actual_hours columns in the project schema.
	
	private BigDecimal getDecimalInput(String prompt) {
	String input = getStringInput(prompt);
		
		if (Objects.isNull(input)) {
			return null;
		}
		
		try {
			return new BigDecimal(input).setScale(2);
			
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
			
		}
		
				
	} // End of getBigDecimal method
	

	// createProject method to create a project using a project name, estimated hours, actual hours, difficulty, and notes
	// for the project.entity/project class.
	private void createProject() {
		String projectName = getStringInput("Enter project name: ");
		BigDecimal estematedHours = getDecimalInput("Enter the estimated hours: ");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours: ");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5): ");
		String notes = getStringInput("Enter project notes: ");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estematedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
		
		
		
	}  // END of createProject Method
	

	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	
	


}

