package services;

import model.Project;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.concurrent.CompletionStage;

/**
 * Created by martin on 12/8/16.
 */
@Singleton
public class ProjectService extends BaseDatabaseService<Project> {

	ProjectService() {
		super("projects", Project.class);
	}

	public CompletionStage<Collection<Project>> getProjects() {
		return this.fetchEntities();
	}
}
