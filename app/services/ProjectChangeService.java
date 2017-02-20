package services;

import model.Converter;
import model.Project;
import model.ProjectChange;
import model.User;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class ProjectChangeService extends BaseDatabaseService<ProjectChange> {

	@Inject AuthService authService;

	public ProjectChangeService() {
		super("project_changes", ProjectChange.class);
	}

	public CompletionStage<List<ProjectChange>> getProjectChanges()
	{
		return this.fetchEntities();
	}

	public CompletionStage<ProjectChange> addProjectChange(Project oldProject)
	{
		Function<User, CompletionStage<ProjectChange>> createProjectChangeByUser = user -> {
			System.err.println("User is in addProjectChange: " + user);

			ProjectChange newProjectChange = ProjectChange.create(oldProject, user);

			return this.addEntity(newProjectChange);
		};

		return authService
				.getLoggedUser()
				.thenComposeAsync(createProjectChangeByUser);
	}

	public CompletionStage<List<ProjectChange>> getProjectChangesForProject(String projectId)
	{
		return this
				.getEntitiesEqualingTo("oldProjectVersion/id", projectId);
	}
}
