package controllers;

import authentication.providers.GoogleProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.*;
import model.User;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.Inject;

import play.libs.ws.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by martin on 11/26/16.
 */
public class AuthController extends Controller {

	public static String SESSION_USER_ID_FIELD = "logged_user_id";
	public static String SESSION_USER_PROVIDER_ID_FIELD = "logged_user_provider_id";
	public static String SESSION_USER_NAME_FIELD = "logged_user_name";

	public static String USER_ID_FIELD = "id";
	public static String USER_PROVIDER_ID_FIELD = "idFromProvider";


	@Inject WSClient ws;
	@Inject HttpExecutionContext ec;
	@Inject GoogleProvider googleProvider;

	public Result google() {
		this.setHeaders();
		return redirect(googleProvider.getRedirectUrl());
	}

	public CompletionStage<Result> handleGoogle() {
		this.setHeaders();
		String code = request().getQueryString("code");
		return googleProvider.handleGoogleAuthentication(code, ec.current())
				.thenApplyAsync(this::getUserInfo, ec.current())
				.thenComposeAsync(this::saveUserInfoInSession, ec.current())
				.thenApplyAsync(user -> redirect("https://morning-taiga-56897.herokuapp.com"));
	}

	private CompletionStage<User> getUserInfo(JsonNode node)
	{
		System.err.println(node);
		final String userProviderId = node.findPath("id").asText();
		final String name = node.findPath("name").asText();
		final CompletableFuture<User> future = new CompletableFuture<>();

		System.err.println("Inside getUserInfo, response is " + userProviderId + " and " + name);

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference usersReference = database.getReference("users");
		Query queryRef = usersReference.orderByChild(USER_PROVIDER_ID_FIELD).equalTo(userProviderId);

		queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (!dataSnapshot.exists()) {
					DatabaseReference userReference = usersReference.push();

					User user = new User();
					user.setName(name);
					user.setId(userReference.getKey());
					user.setIdFromProvider(userProviderId);
					userReference.setValue(user);
					System.err.println("Not success user" + user);

					future.complete(user);
				} else {
					User user = dataSnapshot.getValue(User.class);

					System.err.println("Success user" + user);
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

	private CompletionStage<User> saveUserInfoInSession(CompletionStage<User> future)
	{
		return future.thenApplyAsync(user -> {

			session().put(SESSION_USER_ID_FIELD, user.getId());
			session().put(SESSION_USER_PROVIDER_ID_FIELD, user.getIdFromProvider());
			session().put(SESSION_USER_NAME_FIELD, user.getName());
			System.err.println(session());
			return user;
		}, ec.current());
	}

	public CompletionStage<Result> getLoggedUser() {
		this.setHeaders();
		String id = session(SESSION_USER_ID_FIELD);
		System.err.println("Printing session info: " + session());
		System.out.println(id);
		System.out.println(session());

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference usersReference = database.getReference("users");
		Query queryRef = usersReference.orderByChild(USER_ID_FIELD).equalTo(id);

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
