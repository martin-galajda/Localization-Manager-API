package actions;

import constants.UserRole;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthService;
import services.UserService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AdminAction extends play.mvc.Action.Simple {
	@Inject
	UserService userService;

	@Override
	public CompletionStage<Result> call(Http.Context ctx) {
		Logger.debug("User session is " + ctx.session());
		String userIdFromOAuthProvider = ctx.session().get(AuthService.SESSION_USER_PROVIDER_ID_FIELD);
		Logger.debug("User id from oauth provider is " + userIdFromOAuthProvider);
		if (userIdFromOAuthProvider == null) {
			Result unauthorizedResult = unauthorized();
			return CompletableFuture.completedFuture(unauthorizedResult);
		}

		String userRole = ctx.session().get(AuthService.SESSION_USER_ROLE_FIELD);
		Logger.debug("User role inside session is " + userRole);

		CompletableFuture<Boolean> verifyAdminRole = new CompletableFuture<>();

		if (userRole == null || !userRole.equals(UserRole.ADMIN)) {
			userService.getUserByIdFromProvider(userIdFromOAuthProvider).thenAcceptAsync(user -> {
				if (user.getRole().equals(UserRole.ADMIN)) {
					ctx.session().put(AuthService.SESSION_USER_ROLE_FIELD, user.getRole());
					verifyAdminRole.complete(true);
				} else {
					verifyAdminRole.complete(false);
				}
			});
		} else {
			verifyAdminRole.complete(true);
		}


		return verifyAdminRole.thenComposeAsync(isAdmin -> {
			return isAdmin ? delegate.call(ctx) : CompletableFuture.completedFuture(unauthorized());
		});
	}
}
