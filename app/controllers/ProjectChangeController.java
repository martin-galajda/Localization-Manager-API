package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ProjectChangeService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Controller which encapsulates API endpoint for project history.
 */
public class ProjectChangeController extends Controller{

	@Inject
	ProjectChangeService projectChangeService;

	public CompletionStage<Result> getProjectChangesForProject(String projectId) {
		String endAtId = request().getQueryString("endAtId");
		String limit = request().getQueryString("limit");

		return this
				.projectChangeService
				.getProjectChangesForProject(projectId, endAtId, Integer.parseInt(limit))
				.thenApplyAsync(projectChanges -> ok(Json.toJson(projectChanges)));
	}
}
