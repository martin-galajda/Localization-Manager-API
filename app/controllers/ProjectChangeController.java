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
		return this
				.projectChangeService
				.getProjectChangesForProject(projectId)
				.thenApplyAsync(projectChanges -> ok(Json.toJson(projectChanges)));
	}
}
