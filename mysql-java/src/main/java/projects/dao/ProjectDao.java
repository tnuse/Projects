package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import projects.entity.Category;
import projects.entity.Material;
import projects.entity.Project;
import projects.entity.Step;
import projects.exception.DbException;
import provided.util.DaoBase;



public class ProjectDao extends DaoBase {

	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String STEP_TABLE = "step";
	private static final String PROJECT_TABLE = "project";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	
	
	
	
	
	// insertProject method to create a new project in the project table, using user input for all
	// columns in the project table.
	public Project insertProject(Project project) {
		// @formatter:off
		String sql = ""
				+ "INSERT INTO " + PROJECT_TABLE + " "
				+ "(project_name, estimated_hours, actual_hours, difficulty, notes) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?)";
		// @formatter:on
		
		// try-catch to initiate the connection
		try (Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			
			// try-catch block to add user input data to the prepared SQL statement VALUES
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				setParameter(stmt, 1, project.getProjectName(), String.class);
				setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, project.getDifficulty(), Integer.class);
				setParameter(stmt, 5, project.getNotes(), String.class);
				
				stmt.executeUpdate();
				
				Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
				commitTransaction(conn);
				
				project.setProjectId(projectId);
				return project;
				
			}
			
			catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		} catch (SQLException e) {
			throw new DbException(e);
		}
	} // END of insertProject method




	// fetchAllProjects DAO layer method, called on by ProjectService.java to provide a list of all available projects
	public List<Project> fetchAllProjects() {
		String sql = "SELECT * FROM " + PROJECT_TABLE + " ORDER BY project_name";
		
		try (Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				try(ResultSet rs = stmt.executeQuery()) {
					List<Project> projects = new LinkedList<>();
					
					while (rs.next()) {
						 projects.add(extract(rs, Project.class));
					}
					
					return projects;
				
				}
				
				
			} catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
			
		} catch (SQLException e) {
			throw new DbException(e);
			
		}
	}




	// fetchProjectById method called by the service layer for selection 3, to provide details on the selected project
	public Optional<Project> fetchProjectById(Integer projectId) {
		String sql = "Select * FROM " + PROJECT_TABLE + " WHERE project_id = ?";
		
		try (Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			
			try {
				Project project = null;
				
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				setParameter(stmt, 1, projectId, Integer.class);
						
						try (ResultSet rs = stmt.executeQuery()) {
							if (rs.next()) {
								project = extract(rs, Project.class);
							}
						}
					
					// If statement to gather materials, steps, and categories associated with the selected project ID
					if (Objects.nonNull(project)) {
						project.getMaterials().addAll(fetchProjectMaterials(conn, projectId));
						project.getSteps().addAll(fetchProjectSteps(conn, projectId));
						project.getCategories().addAll(fetchProjectCategories(conn, projectId));
					}
					
					return Optional.ofNullable(project);
				}

			} catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
			
		} catch (SQLException e) {
			throw new DbException(e);
		}
		
	}  // END of fetchProjectById




	// fetchProjectCategories method, called by the fetchProjectById method to provide all related categories from the category table, associated with the selected project id.
	
	private List<Category> fetchProjectCategories(Connection conn, Integer projectId) throws SQLException {
		// @formatter:off
		String sql = ""
			+ "SELECT c.* "
			+ "FROM " + CATEGORY_TABLE + " c "
			+ "JOIN " + PROJECT_CATEGORY_TABLE + " pc USING (category_id) " 
			+ "WHERE project_id = ? ";
		// @formatter:on

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				List<Category> categories = new LinkedList<Category>();
				
				while (rs.next()) {
					categories.add(extract(rs, Category.class));
					
				}
				
				return categories;
			}
		}
	}  // END of fetchProjectCategories method



	// fetchProjectSteps method, called by the fetchProjectById method to provide all related steps from the step table, associated with the selected project id.
	

	private List<Step> fetchProjectSteps(Connection conn, Integer projectId) throws SQLException {
		// @formatter:off
		String sql = "SELECT * FROM " + STEP_TABLE + " WHERE project_id = ?";
		// @formatter:on

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				List<Step> steps = new LinkedList<Step>();
				
				while (rs.next()) {
					steps.add(extract(rs, Step.class));
					
				}
				
				return steps;
			}
		}
	}	// END of fetchProjectSteps


	// fetchProjectMaterials method, called by the fetchProjectById method to provide all related materials from the material table, associated with the selected project id.
	
	private List<Material> fetchProjectMaterials(Connection conn, Integer projectId) throws SQLException {
		// @formatter:off
		String sql = "SELECT * FROM " + MATERIAL_TABLE + " WHERE project_id = ?";

		// @formatter:on
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				List<Material> materials = new LinkedList<Material>();
				
				while (rs.next()) {
					materials.add(extract(rs, Material.class));
					
				}
				
				return materials;
			}
		} 

	} // END of fetchProjectMaterials



	
	public boolean modifyProjectDetails(Project project) {
		// @ formatter.off
		String sql =""
				+ "UPDATE " + PROJECT_TABLE + " SET " 
				+ "project_name = ?, "
				+ "estimated_hours = ?, "
				+ "actual_hours = ?, "
				+ "difficulty = ?, "
				+ "notes = ? "
				+ "WHERE project_id = ?";
		// @ formatter.on

		try (Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				setParameter(stmt, 1, project.getProjectName(), String.class);
				setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, project.getDifficulty(), Integer.class);
				setParameter(stmt, 5, project.getNotes(), String.class);
				setParameter(stmt, 6, project.getProjectId(), Integer.class);
				
				boolean updated = stmt.executeUpdate() == 1;
				commitTransaction(conn);
				
				return updated;
				
			} catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
				
			}
			
			
			
		} catch (SQLException e) {
			throw new DbException(e);
		}
		
	} // END of modifyProjectDetails




	public boolean deleteProject(Integer projectId) {
		String sql = "DELETE FROM " + PROJECT_TABLE + " WHERE project_id = ?";
		
		try (Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				setParameter(stmt, 1, projectId, Integer.class);
				
				boolean deleted = stmt.executeUpdate() == 1;
				
				commitTransaction(conn);
				return deleted;
				
				
				
				
			} catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
			
			
		} catch (SQLException e) {
			throw new DbException(e);
		}
	}

} // END of Class
