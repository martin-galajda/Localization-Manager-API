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
import scala.concurrent.ExecutionContextExecutor;
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
public class HomeController extends Controller {

	private final ExecutionContextExecutor exec;

	@Inject
	public HomeController(ExecutionContextExecutor exec) {
		this.exec = exec;
	}
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
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference projectsReference = database.getReference("projects");
		final CompletableFuture<JsonNode> future = new CompletableFuture<>();

		projectsReference.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				GenericTypeIndicator<HashMap<String, Project>> t = new GenericTypeIndicator<HashMap<String, Project>>() {};
				Object projects = snapshot.getValue(t);
				System.out.println(projects);
				JsonNode node = Json.toJson(projects);
				future.complete(node);
			}

			@Override
			public void onCancelled(DatabaseError err) {
				System.err.println("Database error occured while reading projects: " + err.getMessage());
			}
		});

		return future.thenApplyAsync((jsonNode -> ok(jsonNode)), exec);
    }

	public Result putProject() {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference projectsReference = database.getReference("projects");
		ObjectMapper jsonObjectMapper = new ObjectMapper();
		JsonNode newProjectJson = request().body().asJson();
		try {
			Project newProject = jsonObjectMapper.treeToValue(newProjectJson, Project.class);
			projectsReference.push().setValue(newProject);
			System.err.println(newProject.getName());
		}
		catch (Exception e) {
			System.err.println("Exception occured : " + e);
			return internalServerError(e.toString());
		}


		return ok(newProjectJson);
	}

}
