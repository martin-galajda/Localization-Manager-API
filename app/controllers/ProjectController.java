package controllers;

import actions.UserAction;
import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import play.libs.Json;
import services.AuthService;
import services.ProjectChangeService;
import services.ProjectService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CompletionStage;


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

	@With(UserAction.class)
    public CompletionStage<Result> getProjects() {
		return this.projectService.getProjects().thenApplyAsync(projects -> ok(Json.toJson(projects)));
    }

	@With(UserAction.class)
	public CompletionStage<Result> getProjectsAsHashMap() {
		return this
				.projectService
				.getProjects()
				.thenApplyAsync(projectsList -> {
					HashMap<String, Project> projectHashMap = new HashMap<>();

					Iterator<Project> it = projectsList.iterator();
					while (it.hasNext()) {
						Project nextProject = it.next();
						projectHashMap.put(nextProject.getHashMapIdentifier(), nextProject);
					}

					return projectHashMap;
				})
				.thenApplyAsync(projects -> ok(Json.toJson(projects)));
    }

	@With(UserAction.class)
	public CompletionStage<Result> postProject() {
		JsonNode newProjectJson = request().body().asJson();
		Project newProject = Project.create(newProjectJson);

		if (newProject.getId() == null) {
			return createNewProject(newProject);
		}

		String usernameOfLoggedUser = session().get(AuthService.SESSION_USER_NAME_FIELD);

		return projectService
			.updateProject(newProject, usernameOfLoggedUser)
			.thenApplyAsync(project -> ok(Json.toJson(project)));
	}

	@With(UserAction.class)
	public Result postProjectStatus(String id) {
		JsonNode requestBody = request().body().asJson();

		Integer wordCount = requestBody.get("word_count").asInt(-1);
		String status = requestBody.get("value").asText();

		projectService.updateProjectStatus(id, wordCount, status);
		return ok();
	}

	@With(UserAction.class)
	public Result postProjectPrice(String id) {
		JsonNode requestBody = request().body().asJson();

		Double price = requestBody.get("price").asDouble(-1);
		String currency = requestBody.get("currency").asText("");

		projectService.updateProjectPrice(id, price, currency);
		return ok();
	}

	@With(UserAction.class)
	public CompletionStage<Result> deleteProject(String projectId) {
		return projectService.deleteProject(projectId).thenApplyAsync(deleted -> ok());
	}

	@With(UserAction.class)
	private CompletionStage<Result> createNewProject(Project newProject) {
		return this.projectService.addProject(newProject).thenApplyAsync(entity -> ok(Json.toJson(entity)));
	}

	public Result sessionTest() {

		String host = request().host();

		return ok("Name=" + session(AuthService.SESSION_USER_NAME_FIELD) + "csrf=" + session("csrfToken") + "host= " + host);
	}

}
