package services;

import model.User;

import javax.inject.Inject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Controller.session;


public class AuthService {
	public static String SESSION_USER_ID_FIELD = "logged_user_id";
	public static String SESSION_USER_PROVIDER_ID_FIELD = "logged_user_provider_id";
	public static String SESSION_USER_NAME_FIELD = "logged_user_name";


	@Inject UserService userService;

	public CompletionStage<User> getLoggedUser() {
		System.err.println("Inside get user");

		String userId = play.mvc.Controller.session(SESSION_USER_ID_FIELD);
		CompletableFuture<User> notLoggedInFuture = new CompletableFuture<>();

		System.err.println("Inside auth service userId in session is : " + userId);

		if (userId == null) {
			notLoggedInFuture.complete(null);
			return notLoggedInFuture;
		}

		return userService.getUserById(userId);
	}

}
