package authentication.providers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.*;
import model.User;

import play.Configuration;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.*;

import javax.inject.Inject;

public class GoogleProvider {

	static final String clientId = "1091217744160-poc33mmkke85docb2miaqjtuk8e0ocvp.apps.googleusercontent.com";
	static final String clientSecret = "3djQduYEEVXCJ9kdg4JGC0L2";
	static final String grantType = "authorization_code";

	static final String GOOGLE_REQUEST_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token";

	private String redirectUri;

	private WSClient wsClient;

	private Configuration configuration;

	@Inject
	public GoogleProvider(Configuration configuration, WSClient wsClient) {
		this.wsClient = wsClient;
		this.configuration = configuration;

		this.redirectUri = configuration.getString("authentication.redirectUrl");
	}

	public String getRedirectUrl() {
		String clientId = "1091217744160-poc33mmkke85docb2miaqjtuk8e0ocvp.apps.googleusercontent.com";
		String redirectUri = this.redirectUri;
		String prompt = "consent";
		String responseType = "code";
		String scope = "https://www.googleapis.com/auth/plus.me " +
				"https://www.googleapis.com/auth/userinfo.email " +
				"https://www.googleapis.com/auth/userinfo.profile";
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

	public CompletionStage<JsonNode> handleGoogleAuthentication(String code, Executor executor)
	{
		return this
				.exchangeCodeForToken(code, executor)
				.thenComposeAsync(token -> this.exchangeTokenForUserInfo(token, executor), executor);
	}

	public CompletionStage<WSResponse> exchangeCodeForToken(String code, Executor executor) {
		WSRequest req = wsClient.url(GOOGLE_REQUEST_TOKEN_ENDPOINT);

		String reqForm = "code=" + code +
				"&client_id=" + clientId +
				"&client_secret=" + clientSecret +
				"&grant_type=" + grantType +
				"&redirect_uri=" + redirectUri;

		return req.setContentType("application/x-www-form-urlencoded")
				.post(reqForm)
				.thenApplyAsync(res -> res, executor);
	}

	public CompletionStage<JsonNode> exchangeTokenForUserInfo(WSResponse response, Executor executor) {
		JsonNode jsonBody = response.asJson();
		String accessToken = jsonBody.findPath("access_token").asText();
		String refreshToken = jsonBody.findPath("refresh_token").asText();

		WSRequest accessRequest = wsClient.url("https://www.googleapis.com/oauth2/v2/userinfo");
		WSRequest authReq = accessRequest.setHeader("Authorization", "Bearer " + accessToken);

		CompletionStage<WSResponse> authFuture = authReq.get();

		return authFuture.thenApplyAsync(WSResponse::asJson, executor);
	}

}
