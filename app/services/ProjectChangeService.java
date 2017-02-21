package services;

import model.Project;
import model.ProjectChange;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class ProjectChangeService extends BaseDatabaseService<ProjectChange> {

	@Inject AuthService authService;

	public ProjectChangeService() {
		super("project_changes", ProjectChange.class);
	}

	public CompletionStage<List<ProjectChange>> getProjectChanges()
	{
		return this.fetchEntities();
	}

	public CompletionStage<ProjectChange> addProjectChange(Project oldProject, String usernameOfLoggedUser)
	{
		System.err.println("User is in addProjectChange: " + usernameOfLoggedUser);

		ProjectChange newProjectChange = ProjectChange.create(oldProject, usernameOfLoggedUser);

		return this.addEntity(newProjectChange);
	}

	public CompletionStage<List<ProjectChange>> getProjectChangesForProject(String projectId)
	{
		return this
				.getEntitiesEqualingTo("oldProjectVersion/id", projectId);
	}
}
