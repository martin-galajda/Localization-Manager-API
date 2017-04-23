package actions;

import constants.UserRole;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthService;
import services.ConfigService;
import services.UserService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserAction extends play.mvc.Action.Simple {
	@Inject
	UserService userService;

	@Inject
	ConfigService configService;

	@Override
	public CompletionStage<Result> call(Http.Context ctx) {
		if (ctx.request().hasHeader("Secret-authorization-token")) {
			String authorizationHeader = ctx.request().getHeader("Secret-authorization-token");
			if (authorizationHeader.equals(configService.getSecretAuthorizationToken())) {
				return delegate.call(ctx);
			}
		}


		String userIdFromOAuthProvider = ctx.session().get(AuthService.SESSION_USER_PROVIDER_ID_FIELD);
		if (userIdFromOAuthProvider == null) {
			Result unauthorizedResult = unauthorized();
			return CompletableFuture.completedFuture(unauthorizedResult);
		}
		String userRole = ctx.session().get(AuthService.SESSION_USER_ROLE_FIELD);
		CompletableFuture<Boolean> verifyUserRole = new CompletableFuture<>();

		if (userRole == null || userRole.equals(UserRole.GUEST)) {
			userService.getUserByIdFromProvider(userIdFromOAuthProvider).thenAcceptAsync(user -> {
				if (user.getRole().equals(UserRole.ADMIN) || user.getRole().equals(UserRole.USER)) {
					ctx.session().put(AuthService.SESSION_USER_ROLE_FIELD, user.getRole());
					verifyUserRole.complete(true);
				} else {
					verifyUserRole.complete(false);
				}
			});
		} else {
			verifyUserRole.complete(true);
		}


		return verifyUserRole.thenComposeAsync(hasPermissions -> {
			return hasPermissions ? delegate.call(ctx) : CompletableFuture.completedFuture(unauthorized());
		});
	}
}
