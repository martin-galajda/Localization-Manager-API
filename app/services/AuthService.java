package services;

import model.User;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Inject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Controller.session;


public class AuthService {
	public static String SESSION_USER_ID_FIELD = "logged_user_id";
	public static String SESSION_USER_PROVIDER_ID_FIELD = "logged_user_provider_id";
	public static String SESSION_USER_NAME_FIELD = "logged_user_name";


	@Inject UserService userService;
	@Inject
	HttpExecutionContext ec;

	public CompletionStage<User> getLoggedUser() {
		CompletableFuture<User> loggedUserFuture = new CompletableFuture<>();
		ec.current().execute(() -> {
			System.err.println("Inside get user");

			String userId = play.mvc.Controller.session(SESSION_USER_ID_FIELD);

			System.err.println("Inside auth service userId in session is : " + userId);
			System.err.println(play.mvc.Controller.session());
			System.err.println();

			if (userId == null) {
				loggedUserFuture.complete(null);
			} else {
				userService.getUserById(userId, ec).thenApplyAsync(user -> loggedUserFuture.complete(user));
			}

		});

		return loggedUserFuture;
	}

}
