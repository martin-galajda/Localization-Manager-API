package services;

import exceptions.CompareProjectException;
import model.FieldChange;
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

	public CompletionStage<ProjectChange> addProjectChange(Project newProject, Project oldProject, String usernameOfLoggedUser) throws CompareProjectException
	{
		List<FieldChange> fieldChangeList = newProject.getChangedFields(oldProject);
		ProjectChange newProjectChange = ProjectChange.create(newProject.getId(), fieldChangeList, usernameOfLoggedUser);
		return this.addEntityWithKey(newProject.getId(), newProjectChange);
	}

	public CompletionStage<List<ProjectChange>> getProjectChangesForProject(String projectId, String endAtId, Integer limit)
	{
		return this.getPaginatedEntitiesEqualingTo("projectId", projectId, endAtId, limit);
	}
}
