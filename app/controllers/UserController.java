package controllers;

import actions.AdminAction;
import com.fasterxml.jackson.databind.JsonNode;
import model.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.UserService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Controller which encapsulates API endpoint for users.
 */
public class UserController extends Controller {
	private final UserService userService;

	@Inject
	public UserController(UserService userService) {
		this.userService = userService;
	}

	public CompletionStage<Result> getUsers() {
		return userService.getUsers().thenApplyAsync(users -> {
			return ok(Json.toJson(users));
		});
	}

	public CompletionStage<Result> updateUser(String userId) {
		JsonNode requestBody = request().body().asJson();
		JsonNode isAssignable = requestBody.get("isAssignable");
		CompletableFuture<Result> failedFuture = new CompletableFuture<>();

		if (isAssignable == null) {
			failedFuture.complete(badRequest("`isAssignable` parameter is required."));
			return failedFuture;
		}

		return userService
				.updateUserAssignability(userId, isAssignable.asBoolean())
				.thenApplyAsync(user -> user != null ? ok(Json.toJson(user)) : noContent());
	}

	@With(AdminAction.class)
	public CompletionStage<Result> updateUserRole(String userId) {
		JsonNode requestBody = request().body().asJson();
		String role = requestBody.get("role").asText();
		CompletableFuture<Result> failedFuture = new CompletableFuture<>();

		if (role == null) {
			failedFuture.complete(badRequest("`role` parameter is required."));
			return failedFuture;
		}

		return userService
				.updateUserRole(userId, role)
				.thenApplyAsync(user -> user != null ? ok(Json.toJson(user)) : noContent());
	}

}
