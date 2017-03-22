package controllers;

import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.AuthService;

public class SecuredController extends Security.Authenticator {

	@Override
	public String getUsername(Http.Context ctx) {
		Logger.debug("Inside secured controller");
		Logger.debug(String.format("%s", ctx.session()));
		return ctx.session().get(AuthService.SESSION_USER_NAME_FIELD);
	}

	@Override
	public Result onUnauthorized(Http.Context ctx) {
		return super.onUnauthorized(ctx);
	}
}
