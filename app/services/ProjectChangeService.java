package services;

import model.Converter;
import model.Project;
import model.ProjectChange;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class ProjectChangeService extends BaseDatabaseService<ProjectChange> {

	public ProjectChangeService() {
		super("project_changes", ProjectChange.class);
	}

	public CompletionStage<List<ProjectChange>> getProjectChanges()
	{
		return this.fetchEntities();
	}

	public CompletionStage<ProjectChange> addProjectChange(Project oldProject)
	{
		ProjectChange projectChange = ProjectChange.create(oldProject);
		System.err.println("Adding change inside projectChangeService: addProjectChange");

		return this.addEntity(projectChange);
	}

	public CompletionStage<List<ProjectChange>> getProjectChangesForProject(String projectId)
	{
		return this
				.getEntitiesEqualingTo("oldProjectVersion/id", projectId);
	}
}
