package controllers;

import com.fasterxml.jackson.databind.JsonNode;
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


    public CompletionStage<Result> getProjects() {
		return this.projectService.getProjects().thenApplyAsync(projects -> ok(Json.toJson(projects)));
    }

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

	public CompletionStage<Result> postProject() {
		JsonNode newProjectJson = request().body().asJson();
		Project newProject = Project.create(newProjectJson);

		if (newProject.getId() == null) {
			return createNewProject(newProject);
		}

		String usernameOfLoggedUser = session().get(AuthService.SESSION_USER_NAME_FIELD);
		System.err.println("Inside post project seesion is: " + session());
		System.err.println("Inside post project user name from session: " + usernameOfLoggedUser);

		return projectService
			.updateProject(newProject, usernameOfLoggedUser)
			.thenApplyAsync(project -> ok(Json.toJson(project)));
	}

	public Result postProjectStatus(String id) {
		JsonNode newProjectJson = request().body().asJson();
		Integer wordCount = newProjectJson.get("word_count").asInt();
		String status = newProjectJson.get("status").asText();

		projectService.updateProjectStatus(id, wordCount, status);
		return ok();
	}

	public CompletionStage<Result> deleteProject(String projectId) {
		return projectService.deleteProject(projectId).thenApplyAsync(deleted -> ok());
	}

	private CompletionStage<Result> createNewProject(Project newProject) {
		return this.projectService.addProject(newProject).thenApplyAsync(entity -> ok(Json.toJson(entity)));
	}

	public Result sessionTest() {
		return ok("Name=" + session(AuthService.SESSION_USER_NAME_FIELD) + "csrf=" + session("csrfToken"));
	}

}
