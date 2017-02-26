package services;

import exceptions.CompareProjectException;
import model.Project;
import play.Logger;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ProjectService extends BaseDatabaseService<Project> {

	@Inject ProjectChangeService projectChangeService;

	public ProjectService() {
		super("projects", Project.class);
	}

	public CompletionStage<List<Project>> getProjects() {
		return this.fetchEntities();
	}

	public CompletionStage<Project> addProject(Project project) {
		return this.addEntity(project);
	}

	public CompletionStage<Project> updateProject(Project newProject, final String usernameOfLoggedUser) {
		saveProjectChange(newProject, usernameOfLoggedUser);
		return this.updateEntity(newProject);
	}

	public CompletionStage<Project> getProjectById(String projectId) {
		return this.getOneEntityEqualingTo("id", projectId);
	}

	public void updateProjectStatus(String entityId, Integer wordCount, String status) {
		HashMap<String, Object> updates = new HashMap<>();
		updates.put("wordCount", wordCount);
		updates.put("status", status);

		this.updateEntityFields(entityId, updates);
	}

	public CompletionStage<Boolean> deleteProject(String projectId) {
		return this.deleteEntity(projectId);
	}

	private void saveProjectChange(Project newProject, String usernameOfLoggedUser) {
		this.getProjectById(newProject.getId()).thenAcceptAsync(oldProject -> {
			try {
				this.projectChangeService.addProjectChange(newProject, oldProject, usernameOfLoggedUser);
			} catch (CompareProjectException e) {
				Logger.error(e.getMessage());
			}
		});
	}
}
