package actions;

import play.mvc.Http;
import play.mvc.Result;
import services.AuthService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class GuestUserAction extends play.mvc.Action.Simple {
	@Override
	public CompletionStage<Result> call(Http.Context ctx) {
		String userIdFromOAuthProvider = ctx.session().get(AuthService.SESSION_USER_PROVIDER_ID_FIELD);
		if (userIdFromOAuthProvider == null) {
			Result unauthorizedResult = unauthorized();
			return CompletableFuture.completedFuture(unauthorizedResult);
		}
		return delegate.call(ctx);
	}
}
