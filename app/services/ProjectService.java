package services;

import model.Project;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by martin on 12/8/16.
 */
@Singleton
public class ProjectService extends BaseDatabaseService<Project> {

	public ProjectService() {
		super("projects", Project.class);
	}

	public CompletionStage<List<Project>> getProjects() {
		return this.fetchEntities();
	}

	public CompletionStage<Project> addProject(Project project) {
		return this.addEntity(project);
	}

	public CompletionStage<Project> updateProject(Project project) {
		return this.updateEntity(project);
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
