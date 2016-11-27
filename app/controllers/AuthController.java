package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.*;
import play.filters.csrf.*;

import javax.inject.Inject;

import play.libs.ws.*;
import scala.concurrent.ExecutionContextExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import java.security.SecureRandom;

/**
 * Created by martin on 11/26/16.
 */
public class AuthController extends Controller {

	@Inject WSClient ws;
	@Inject ExecutionContextExecutor exec;

	public Result google() {
		SecureRandom randomGenerator = new SecureRandom();
		int state = randomGenerator.nextInt();
		System.out.println("google");
		System.err.println("google");

		String clientSecret = "3djQduYEEVXCJ9kdg4JGC0L2";
		String clientId = "1091217744160-poc33mmkke85docb2miaqjtuk8e0ocvp.apps.googleusercontent.com";
		String redirectUri = "https://glacial-hollows-97055.herokuapp.com/auth/google/handler";
		String prompt = "consent";
		String responseType = "code";
		String scope = "https://www.googleapis.com/auth/plus.me";
		String accessType = "offline";

		String googleUrl = "https://accounts.google.com/o/oauth2/v2/auth";

		final CompletableFuture<JsonNode> future = new CompletableFuture<>();

		WSRequest req = ws.url(googleUrl)
				.setQueryParameter("redirect_uri", redirectUri)
				.setQueryParameter("client_id", clientId)
				.setQueryParameter("prompt", prompt)
				.setQueryParameter("response_type", responseType)
				.setQueryParameter("scope", scope)
				.setQueryParameter("access_type", accessType);

		googleUrl += "?" +
				"&redirect_uri=" + redirectUri +
				"&client_id=" + clientId +
				"&prompt=" + prompt +
				"&response_type=" + responseType +
				"&scope=" + scope +
				"&access_type=" + accessType;


		return redirect(googleUrl);
	}

	public CompletionStage<Result> handleGoogle() {
		System.out.println("handle google");
		System.out.println(request());
		System.err.println("handle google");
		System.err.println(request());
		String code = request().getQueryString("code");
		String clientId = "1091217744160-poc33mmkke85docb2miaqjtuk8e0ocvp.apps.googleusercontent.com";
		String clientSecret = "3djQduYEEVXCJ9kdg4JGC0L2";
		String grantType = "authorization_code";
		String redirectUri = "https://morning-taiga-56897.herokuapp.com/";

		WSRequest req = ws.url("https://accounts.google.com/o/oauth2/token");
		final CompletableFuture<JsonNode> future = new CompletableFuture<>();

		JsonNode reqBody = Json.newObject()
				.put("code", code)
				.put("client_id", clientId)
				.put("client_secret", clientSecret)
				.put("grant_type", grantType)
				.put("redirect_uri", redirectUri);

		req.post(reqBody).thenApply(response -> {
			JsonNode jsonBody = response.asJson();
			String accessToken = jsonBody.findPath("access_token").asText();
			System.out.println(accessToken);
			String refreshToken = jsonBody.findPath("refresh_token").asText();

			WSRequest accessRequest = ws.url("https://www.googleapis.com/oauth2/v2/userinfo");
			CompletionStage<WSResponse> authFuture = accessRequest.setHeader("Authorization", "Bearer " + accessToken).get();


			authFuture.thenApply(res -> {
				JsonNode jsonBodyRes = res.asJson();
				System.out.println(jsonBodyRes);
				String name = jsonBodyRes.findPath("name").asText();
				String id = jsonBodyRes.findPath("id").asText();
				System.out.println(name);
				System.out.println(id);
				future.complete(jsonBodyRes);
				return jsonBodyRes;
			});
			return jsonBody;
		});

		return future.thenApplyAsync(res -> ok(res), exec);
	}

	public Result googleSuccess() {
		return ok();
	}
}
