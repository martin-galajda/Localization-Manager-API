package services;

import model.Project;
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

	public CompletionStage<Project> updateProject(Project project, String usernameOfLoggedUser) {

		CompletableFuture<Boolean> createdProjectChange = new CompletableFuture<>();
		this.getProjectById(project.getId()).thenAcceptAsync(oldProject -> {
			if (!createdProjectChange.isDone()) {
				System.err.println("Adding change");
				this.projectChangeService
						.addProjectChange(oldProject, usernameOfLoggedUser)
						.thenApplyAsync(projectChange -> createdProjectChange.complete(true));
			}
		});

		return createdProjectChange.thenComposeAsync((createdChange) -> this.updateEntity(project));
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
}
