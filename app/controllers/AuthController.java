package controllers;

import authentication.providers.GoogleProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.*;
import constants.UserRole;
import model.User;
import play.*;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.Inject;

import play.libs.ws.*;
import services.AuthService;
import services.UserService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import play.Configuration;


public class AuthController extends Controller {

	@Inject WSClient ws;
	@Inject HttpExecutionContext ec;
	@Inject GoogleProvider googleProvider;
	@Inject UserService userService;

	private Configuration configuration;

	@Inject
	public AuthController(Configuration configuration) {
		this.configuration = configuration;
		play.Logger.debug("User info: " + this.configuration);
	}

	public Result google() {
		return redirect(googleProvider.getRedirectUrl());
	}

	public CompletionStage<Result> handleGoogle() {
		String code = request().getQueryString("code");
		String host = request().host();
		String protocol = "https";
		String serverUri = protocol + "://www." + host;

		return googleProvider
				.handleGoogleAuthentication(code, serverUri, ec.current())
				.thenApplyAsync(this::getUserInfo, ec.current())
				.thenComposeAsync(this::saveUserInfoInSession, ec.current())
				.thenApplyAsync(user -> redirect("https://morning-taiga-56897.herokuapp.com"));
	}

	private CompletionStage<User> getUserInfo(JsonNode node)
	{
		final String userProviderId = node.findPath("id").asText();
		final String name = node.findPath("name").asText();
		final String newPictureUrl = node.findPath("picture").asText();
		final String newEmail = node.findPath("email").asText();
		final CompletableFuture<User> future = new CompletableFuture<>();

		play.Logger.debug("User info: " + node);

		userService.getUserByIdFromProvider(userProviderId).thenAcceptAsync(user -> {
			play.Logger.debug("User: " + user);
			if (user == null) {
				User newUser = new User();
				newUser.setName(name);
				newUser.setIdFromProvider(userProviderId);
				newUser.setPictureUrl(newPictureUrl);
				newUser.setEmail(newEmail);

				if (this.isAdministrator(newEmail)) {
					newUser.setRole(UserRole.ADMIN);
				} else {
					newUser.setRole(UserRole.GUEST);
				}
				play.Logger.debug("New user: " + newUser);

				userService.add(newUser).thenAcceptAsync(future::complete);
			} else {
				future.complete(user);
			}
		});

		return future;
	}

	private String[] getAdministratorEmailsFromConfig()
	{
		return this.configuration.getString("administrators").split(",");
	}

	private Boolean isAdministrator(String email)
	{
		play.Logger.debug("getting administrators emails: ");

		String[] adminEmails = getAdministratorEmailsFromConfig();
		play.Logger.debug("got emails: ");

		for (String adminEmail : adminEmails) {
			if (adminEmail.equals(email)) {
				return true;
			}
		}

		return false;
	}

	private CompletionStage<User> saveUserInfoInSession(CompletionStage<User> future)
	{
		return future.thenApplyAsync(user -> {
			session().put(AuthService.SESSION_USER_ID_FIELD, user.getId());
			session().put(AuthService.SESSION_USER_PROVIDER_ID_FIELD, user.getIdFromProvider());
			session().put(AuthService.SESSION_USER_NAME_FIELD, user.getName());
			session().put(AuthService.SESSION_USER_ROLE_FIELD, user.getRole());
			return user;
		}, ec.current());
	}

	public CompletionStage<Result> getLoggedUser() {
		String id = session(AuthService.SESSION_USER_ID_FIELD);
		final CompletableFuture<JsonNode> authorizedFuture = new CompletableFuture<>();
		final CompletableFuture<Result> unauthorizedFuture = new CompletableFuture<>();

		if (id == null) {
			unauthorizedFuture.complete(unauthorized());
			return unauthorizedFuture;
		}

		userService.getUserById(id).thenAcceptAsync(user -> {
			authorizedFuture.complete(Json.toJson(user));
		});

		return authorizedFuture.thenApplyAsync(jsonNode -> jsonNode != null ? ok(jsonNode) : notFound());
	}

	public Result logout() {
		session().clear();

		return ok();
	}
}
