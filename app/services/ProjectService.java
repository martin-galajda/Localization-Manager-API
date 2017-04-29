package services;

import exceptions.CompareProjectException;
import model.Project;
import model.ProjectChange;
import model.TranslationStatus;
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
		return this
				.saveProjectChange(newProject, usernameOfLoggedUser)
				.thenComposeAsync((updated) -> this.updateEntity(newProject));
	}

	public CompletionStage<Project> getProjectById(String projectId) {
		return this.getOneEntityEqualingTo("id", projectId);
	}

	public void updateProjectStatus(String entityId, Integer wordCount, String translationStatus) {
		HashMap<String, Object> updates = new HashMap<>();

		if (wordCount == null || !wordCount.equals(-1)) {
			updates.put("wordCount", wordCount);
		}
		updates.put("status", translationStatus);

		this.updateEntityFields(entityId, updates);
	}

	public void updateProjectPrice(String entityId, Double price, String currency) {
		HashMap<String, Object> updates = new HashMap<>();

		if (price != null && !price.equals(-1)) {
			updates.put("price", price);
		}
		if (currency != null && !currency.equals("")) {
			updates.put("currency", currency);
		}

		this.updateEntityFields(entityId, updates);
	}

	public CompletionStage<Boolean> deleteProject(String projectId) {
		return this.deleteEntity(projectId);
	}

	private CompletionStage<Boolean> saveProjectChange(Project newProject, String usernameOfLoggedUser) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();

		this.getProjectById(newProject.getId()).thenAcceptAsync(oldProject -> {
			try {
				this.projectChangeService
						.addProjectChange(newProject, oldProject, usernameOfLoggedUser)
						.thenAcceptAsync(projectChange -> {
							future.complete(true);
						});
			} catch (CompareProjectException e) {
				Logger.error(e.getMessage());
				future.complete(false);
			}
		});

		return future;
	}
}
