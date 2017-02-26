package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ProjectChangeService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;


public class ProjectChangeController extends Controller{

	@Inject
	ProjectChangeService projectChangeService;

	public CompletionStage<Result> getProjectChangesForProject(String projectId) {
		String startAtId = request().getQueryString("startAt");
		String limit = request().getQueryString("limit");

		return this
				.projectChangeService
				.getProjectChangesForProject(projectId, startAtId, Integer.parseInt(limit))
				.thenApplyAsync(projectChanges -> ok(Json.toJson(projectChanges)));
	}
}
