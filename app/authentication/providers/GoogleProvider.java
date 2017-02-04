package authentication.providers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.*;
import model.User;

import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.*;

import javax.inject.Inject;

public class GoogleProvider {

	static final String clientId = "1091217744160-poc33mmkke85docb2miaqjtuk8e0ocvp.apps.googleusercontent.com";
	static final String clientSecret = "3djQduYEEVXCJ9kdg4JGC0L2";
	static final String grantType = "authorization_code";
	static final String redirectUri = "https://glacial-hollows-97055.herokuapp.com/auth/google/handler";

	static final String GOOGLE_REQUEST_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token";

	@Inject
	private WSClient wsClient;

	//@Inject
	//private HttpExecutionContext ec;

	public GoogleProvider() {

	}

	public String getRedirectUrl() {
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

		return googleUrl;
	}

	public CompletionStage<JsonNode> handleGoogleAuthentication(String code, HttpExecutionContext ec)
	{
		return this
				.exchangeCodeForToken(code, ec)
				.thenComposeAsync(this::exchangeTokenForUserInfo, ec.current());
	}

	public CompletionStage<WSResponse> exchangeCodeForToken(String code, HttpExecutionContext ec) {

		// todo check if this works, maybe replace with query string extracing
	//	String code = response.asJson().findPath("code").asText();
		WSRequest req = wsClient.url(GOOGLE_REQUEST_TOKEN_ENDPOINT);
		final CompletableFuture<JsonNode> future = new CompletableFuture<>();

		String reqForm = "code=" + code +
				"&client_id=" + clientId +
				"&client_secret=" + clientSecret +
				"&grant_type=" + grantType +
				"&redirect_uri=" + redirectUri;

		return req.setContentType("application/x-www-form-urlencoded")
				.post(reqForm)
				.thenApplyAsync(res -> res, ec.current());
	}

	public CompletionStage<JsonNode> exchangeTokenForUserInfo(WSResponse response) {
		JsonNode jsonBody = response.asJson();
		String accessToken = jsonBody.findPath("access_token").asText();
		String refreshToken = jsonBody.findPath("refresh_token").asText();

		WSRequest accessRequest = wsClient.url("https://www.googleapis.com/oauth2/v2/userinfo");
		WSRequest authReq = accessRequest.setHeader("Authorization", "Bearer " + accessToken);

		CompletionStage<WSResponse> authFuture = authReq.get();

		return authFuture.thenApplyAsync(WSResponse::asJson);
	}
/*
	public void handleReceivedUserInfo(WSResponse res) {
		JsonNode jsonBodyRes = res.asJson();
		String name = jsonBodyRes.findPath("name").asText();
		String id = jsonBodyRes.findPath("id").asText();
		//future.complete(jsonBodyRes);
		return jsonBodyRes;
	}
*/

}
