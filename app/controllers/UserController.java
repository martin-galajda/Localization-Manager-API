package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by martin on 12/8/16.
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
		Boolean isAssignable = requestBody.get("isAssignable").asBoolean();
		CompletableFuture failedFuture = new CompletableFuture();

		if (isAssignable == null) {
			failedFuture.complete(forbidden("`isAssignable` parameter is required."));
			return failedFuture;
		}

		return userService
				.updateUserAssignability(userId, isAssignable)
				.thenApplyAsync(user -> user != null ? ok(Json.toJson(user)) : noContent());
	}

}
