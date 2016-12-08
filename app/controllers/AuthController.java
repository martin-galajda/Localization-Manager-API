package controllers;

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

	public Result google() {
		String clientId = "1091217744160-poc33mmkke85docb2miaqjtuk8e0ocvp.apps.googleusercontent.com";
		String redirectUri = "https://glacial-hollows-97055.herokuapp.com/auth/google/handler";
		String prompt = "consent";
		String responseType = "code";
		String scope = "https://www.googleapis.com/auth/plus.me";
		String accessType = "offline";

		String googleUrl = "https://accounts.google.com/o/oauth2/v2/auth";


		googleUrl += "?" +
				"redirect_uri=" + redirectUri +
				"&client_id=" + clientId +
				"&prompt=" + prompt +
				"&response_type=" + responseType +
				"&scope=" + scope +
				"&access_type=" + accessType;


		return redirect(googleUrl);
	}

	public CompletionStage<Result> handleGoogle() {
		String code = request().getQueryString("code");
		String clientId = "1091217744160-poc33mmkke85docb2miaqjtuk8e0ocvp.apps.googleusercontent.com";
		String clientSecret = "3djQduYEEVXCJ9kdg4JGC0L2";
		String grantType = "authorization_code";
		String redirectUri = "https://glacial-hollows-97055.herokuapp.com/auth/google/handler";

		WSRequest req = ws.url("https://accounts.google.com/o/oauth2/token");
		final CompletableFuture<JsonNode> future = new CompletableFuture<>();

		String reqForm = "code=" + code +
				"&client_id=" + clientId +
				"&client_secret=" + clientSecret +
				"&grant_type=" + grantType +
				"&redirect_uri=" + redirectUri;

		req.setContentType("application/x-www-form-urlencoded").post(reqForm).thenApplyAsync(response -> {
			JsonNode jsonBody = response.asJson();
			String accessToken = jsonBody.findPath("access_token").asText();
			String refreshToken = jsonBody.findPath("refresh_token").asText();

			WSRequest accessRequest = ws.url("https://www.googleapis.com/oauth2/v2/userinfo");
			WSRequest authReq = accessRequest.setHeader("Authorization", "Bearer " + accessToken);

			CompletionStage<WSResponse> authFuture = authReq.get();

			authFuture.thenApplyAsync(res -> {
				JsonNode jsonBodyRes = res.asJson();
				String name = jsonBodyRes.findPath("name").asText();
				String id = jsonBodyRes.findPath("id").asText();
				future.complete(jsonBodyRes);
				return jsonBodyRes;
			}, ec.current());
			return jsonBody;
		}, ec.current());

		return future.thenApplyAsync(res -> {
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
		}, ec.current());
	}

	public CompletionStage<Result> getLoggedUser() {
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
}
