package controllers;

import authentication.providers.GoogleProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.*;
import model.Project;
import model.User;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import play.filters.csrf.*;

import javax.inject.Inject;

import play.libs.ws.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import java.security.SecureRandom;

/**
 * Created by martin on 11/26/16.
 */
public class AuthController extends Controller {

	@Inject WSClient ws;
	@Inject HttpExecutionContext ec;
	@Inject GoogleProvider googleProvider;

	public Result google() {
		this.setHeaders();
		return redirect(googleProvider.getRedirectUrl());
	}

	public Result handleGoogle() {
		this.setHeaders();
		String code = request().getQueryString("code");
		googleProvider.handleGoogleAuthentication(code)
				.thenApplyAsync(this::saveUser);

		/*return future.thenApplyAsync(res -> {
			String id = res.findPath("id").asText();
			String name = res.findPath("name").asText();

			FirebaseDatabase database = FirebaseDatabase.getInstance();
			DatabaseReference usersReference = database.getReference("users");
			Query queryRef = usersReference.orderByChild("id").equalTo(id);

			queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					if (!dataSnapshot.exists()) {
						User user = new User();
						user.setId(id);
						user.setName(name);

						usersReference.push().setValue(user);
					}
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {
					System.err.println(databaseError);
				}
			});

			session().put("logged_user_id", id);

			System.err.println("In redirecting after auth Session is: " + session());


			return redirect("https://morning-taiga-56897.herokuapp.com");
		}, ec.current());*/
		return redirect("https://morning-taiga-56897.herokuapp.com");
	}

	private CompletionStage<User> saveUser(JsonNode node)
	{
		String id = node.findPath("id").asText();
		final String name = node.findPath("name").asText();
		final CompletableFuture<User> future = new CompletableFuture<>();
		
		System.err.println("Inside saveUser, response is " + id + " and " + name);

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference usersReference = database.getReference("users");
		Query queryRef = usersReference.orderByChild("id").equalTo(id);

		queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (!dataSnapshot.exists()) {
					DatabaseReference userReference = usersReference.push();

					User user = new User();
					user.setName(name);
					user.setId(userReference.getKey());
					userReference.setValue(user);

					future.complete(user);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				System.err.println(databaseError);
			}
		});

		return future;
	}

	public CompletionStage<Result> getLoggedUser() {
		this.setHeaders();
		String id = session("logged_user_id");
		System.err.println("Printing session info: " + session());
		System.out.println(id);
		System.out.println(session());

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference usersReference = database.getReference("users");
		Query queryRef = usersReference.orderByChild("id").equalTo(id);

		final CompletableFuture<JsonNode> future = new CompletableFuture<>();

		response().setHeader("Access-Control-Allow-Origin", "https://morning-taiga-56897.herokuapp.com");
		response().setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, PUT");
		response().setHeader("Access-Control-Allow-Headers", "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With, X-XSRF-TOKEN");
		response().setHeader("Access-Control-Allow-Credentials", "true");

		queryRef.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				if (!snapshot.exists()) {
					future.complete(null);
				}
				GenericTypeIndicator<HashMap<String, User>> t = new GenericTypeIndicator<HashMap<String, User>>() {};
				HashMap<String, User> userMap = snapshot.getValue(t);
				if (userMap.size() != 1) {
					future.complete(null);
				}

				Iterator<User> userIt = userMap.values().iterator();

				JsonNode node = Json.toJson(userIt.next());
				future.complete(node);
			}

			@Override
			public void onCancelled(DatabaseError err) {
				System.err.println("Database error occured while reading user: " + err.getMessage());
			}
		});

		return future.thenApplyAsync((jsonNode -> {
			return jsonNode != null ? ok(jsonNode) : noContent();
		}), ec.current());
	}


	public Result googleSuccess() {
		return ok("Sucess");
	}

	public Result logout() {
		session().clear();

		return ok();
	}

	private void setHeaders() {
		response().setHeader("Access-Control-Allow-Origin", "https://morning-taiga-56897.herokuapp.com");
		response().setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, PUT");
		response().setHeader("Access-Control-Allow-Headers", "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With, X-XSRF-TOKEN");
		response().setHeader("Access-Control-Allow-Credentials", "true");
	}
}
