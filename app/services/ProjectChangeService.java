package services;

import model.Converter;
import model.Project;
import model.ProjectChange;
import model.User;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class ProjectChangeService extends BaseDatabaseService<ProjectChange> {

	@Inject AuthService authService;
	@Inject
	HttpExecutionContext ec;

	public ProjectChangeService() {
		super("project_changes", ProjectChange.class);
	}

	public CompletionStage<List<ProjectChange>> getProjectChanges()
	{
		return this.fetchEntities();
	}

	public CompletionStage<ProjectChange> addProjectChange(Project oldProject, HttpExecutionContext ec)
	{
		Function<User, CompletionStage<ProjectChange>> createProjectChangeByUser = user -> {
			System.err.println("User is in addProjectChange: " + user);

			ProjectChange newProjectChange = ProjectChange.create(oldProject, user);

			return this.addEntity(newProjectChange);
		};

		return authService
				.getLoggedUser(ec)
				.thenComposeAsync(createProjectChangeByUser, ec.current());
	}

	public CompletionStage<List<ProjectChange>> getProjectChangesForProject(String projectId)
	{
		return this
				.getEntitiesEqualingTo("oldProjectVersion/id", projectId);
	}
}
