package authentication.providers;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import com.fasterxml.jackson.databind.JsonNode;

import play.*;
import play.libs.ws.*;
import services.ConfigService;

import javax.inject.Inject;

public class GoogleProvider {
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

	/**
	 * Build URL that is used by Google for sending authorization code (grant)
	 * after user grants permission to access his information to the application.
	 * @return URL to which will Google redirect request with authorization code.
	 */
	public String getRedirectUrl() {
		String clientId = configuration.getString("google.oauth2.clientId");
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

	/**
	 * Handles user authentication via Google.
	 * It receives authorization code (grant) which is consequently exchanged for the access token
	 * which is then exchanged for the user information.
	 * @param code
	 * @param executor
	 * @return Response containing user information.
	 */
	public CompletionStage<JsonNode> handleGoogleAuthentication(String code, Executor executor)
	{
		return this
				.exchangeCodeForToken(code, executor)
				.thenComposeAsync(token -> this.exchangeTokenForUserInfo(token, executor), executor);
	}

	/**
	 * Exchanges authorization code for the access token
	 * @param code
	 * @param executor
	 * @return Response containing access token.
	 */
	public CompletionStage<WSResponse> exchangeCodeForToken(String code, Executor executor) {
		WSRequest req = wsClient.url(GOOGLE_REQUEST_TOKEN_ENDPOINT);
		String clientId = configuration.getString("google.oauth2.clientId");
		String clientSecret = configuration.getString("google.oauth2.clientSecret");
		String reqForm = "code=" + code +
				"&client_id=" + clientId +
				"&client_secret=" + clientSecret +
				"&grant_type=" + grantType +
				"&redirect_uri=" + this.redirectUri;

		return req.setContentType("application/x-www-form-urlencoded")
				.post(reqForm)
				.thenApplyAsync(res -> res, executor);
	}

	/**
	 * Exchanges access token for the user information.
	 * @param response
	 * @param executor
	 * @return Response containing user information.
	 */
	public CompletionStage<JsonNode> exchangeTokenForUserInfo(WSResponse response, Executor executor) {
		JsonNode jsonBody = response.asJson();
		String accessToken = jsonBody.findPath("access_token").asText();


		WSRequest accessRequest = wsClient.url("https://www.googleapis.com/oauth2/v2/userinfo");
		WSRequest authReq = accessRequest.setHeader("Authorization", "Bearer " + accessToken);

		CompletionStage<WSResponse> authFuture = authReq.get();

		return authFuture.thenApplyAsync(WSResponse::asJson, executor);
	}

}
