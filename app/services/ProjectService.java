package services;

import model.Project;

import javax.inject.Singleton;
import java.util.Collection;
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

	public CompletionStage<Project> updateProject(String projectId, Project project) {
		return this.updateEntity(project);
	}
}
