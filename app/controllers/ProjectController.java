package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.firebase.*;
import play.mvc.*;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import services.ProjectService;
import views.html.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import model.*;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ProjectController extends Controller {

	@Inject
	private ProjectService projectService;

    public Result index() {
        //return ok(index.render("Your new application is ready."));
		return ok();
    }


    public CompletionStage<Result> getProjects() {
		return this.projectService.getProjects().thenApplyAsync(projects -> ok(Json.toJson(projects)));
    }

	public CompletionStage<Result> postProject() {
		JsonNode newProjectJson = request().body().asJson();
		Project newProject = Project.create(newProjectJson);

		if (newProject.getId() == null) {
			return createNewProject(newProject);
		}

		return projectService
			.updateProject(newProject)
			.thenApplyAsync(project -> ok(Json.toJson(project)));
	}

	public CompletionStage<Result> deleteProject() {
		String projectId = request().getQueryString("id");

		return projectService.deleteProject(projectId).thenApplyAsync(deleted -> ok());
	}

	private CompletionStage<Result> createNewProject(Project newProject) {
		return this.projectService.addProject(newProject).thenApplyAsync(entity -> ok(Json.toJson(entity)));
	}

	public Result sessionTest() {
		return ok("Name=" + session("name") + "csrf=" + session("csrfToken"));
	}

}
