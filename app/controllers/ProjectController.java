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
	private HttpExecutionContext ec;

	@Inject
	private ProjectService projectService;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }


    public CompletionStage<Result> getProjects() {
		return this.projectService.getProjects().thenApplyAsync(projects -> ok(Json.toJson(projects)));
    }

	public Result postProject() {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference projectsReference = database.getReference("projects");
		ObjectMapper jsonObjectMapper = new ObjectMapper();
		JsonNode newProjectJson = request().body().asJson();

		response().setHeader("Access-Control-Allow-Origin", "*");
		response().setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, PUT");

		try {
			Project newProject = jsonObjectMapper.treeToValue(newProjectJson, Project.class);
			if (newProject.getId() == null) {
				return createNewProject(newProject);
			}
			Map <String, Object> projectUpdates = new HashMap<>();
			projectUpdates.put(newProject.getId(), newProject);
			projectsReference.updateChildren(projectUpdates);
		}
		catch (Exception e) {
			System.err.println("Exception occured : " + e);
			return internalServerError(e.toString());
		}


		return ok(newProjectJson);
	}

	private Result createNewProject(Project newProject) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference projectsReference = database.getReference("projects");
		DatabaseReference newProjectRef = projectsReference.push();
		newProjectRef.setValue(newProject);
		newProject.setId(newProjectRef.getKey());
		JsonNode newProjectJson = Json.toJson(newProject);
		return ok(newProjectJson);
	}

	public Result options() {
		return options("/");
	}

	public Result options(String s) {
		response().setHeader("Access-Control-Allow-Origin", "https://morning-taiga-56897.herokuapp.com");
		response().setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, PUT");
		response().setHeader("Access-Control-Allow-Headers", "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With, X-XSRF-TOKEN");
		response().setHeader("Access-Control-Allow-Credentials", "true");

		return ok();
	}


	public Result sessionTest() {
		return ok("Name=" + session("name") + "csrf=" + session("csrfToken"));
	}

}
