package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.*;
import play.filters.csrf.*;

import javax.inject.Inject;

import play.libs.ws.*;
import java.util.concurrent.CompletionStage;

import java.security.SecureRandom;

/**
 * Created by martin on 11/26/16.
 */
public class AuthController extends Controller {

	@Inject WSClient ws;


	public CompletionStage<WSResponse> google() {
		SecureRandom randomGenerator = new SecureRandom();
		int state = randomGenerator.nextInt();

		String clientSecret = "3djQduYEEVXCJ9kdg4JGC0L2";
		String clientId = "1091217744160-poc33mmkke85docb2miaqjtuk8e0ocvp.apps.googleusercontent.com";
		String redirectUri = "https://glacial-hollows-97055.herokuapp.com/auth/google/handler";
		String prompt = "consent";
		String responseType = "code";
		String scope = "https://www.googleapis.com/auth/plus.me";
		String accessType = "offline";

		String googleUrl = "https://accounts.google.com/o/oauth2/v2/auth";

		WSRequest req = ws.url(googleUrl)
				.setQueryParameter("redirect_uri", redirectUri)
				.setQueryParameter("client_id", clientId)
				.setQueryParameter("prompt", prompt)
				.setQueryParameter("response_type", responseType)
				.setQueryParameter("scope", scope)
				.setQueryParameter("access_type", accessType);


		return req.setFollowRedirects(true).get();
	}

	public CompletionStage<CompletionStage<JsonNode>> handleGoogle() {
		System.out.println("handle google");
		System.out.println(request());
		String code = request().getQueryString("code");
		String clientId = "1091217744160-poc33mmkke85docb2miaqjtuk8e0ocvp.apps.googleusercontent.com";
		String clientSecret = "3djQduYEEVXCJ9kdg4JGC0L2";
		String grantType = "authorization_code";
		String redirectUri = "https://morning-taiga-56897.herokuapp.com/";

		WSRequest req = ws.url("https://accounts.google.com/o/oauth2/token");

		JsonNode reqBody = Json.newObject()
				.put("code", code)
				.put("client_id", clientId)
				.put("client_secret", clientSecret)
				.put("grant_type", grantType)
				.put("redirect_uri", redirectUri);

		req.post(reqBody);

		return req.post(reqBody).thenApply(response -> {
			JsonNode jsonBody = response.asJson();
			String accessToken = jsonBody.findPath("access_token").asText();
			System.out.println(accessToken);
			String refreshToken = jsonBody.findPath("refresh_token").asText();

			WSRequest accessRequest = ws.url("https://www.googleapis.com/oauth2/v2/userinfo");
			CompletionStage<WSResponse> future = accessRequest.setHeader("Authorization", "Bearer " + accessToken).get();

			return future.thenApply(res -> {
				JsonNode jsonBodyRes = res.asJson();
				System.out.println(jsonBodyRes);
				String name = jsonBodyRes.findPath("name").asText();
				String id = jsonBodyRes.findPath("id").asText();
				System.out.println(name);
				System.out.println(id);

				return jsonBody;
			});
		});
	}

	public Result googleSuccess() {
		return ok();
	}
}
