package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controller which encapsulates shared application logic (e.g. handling OPTIONS requests).
 */
public class AppController extends Controller {
	public Result options() {
		return options("/");
	}

	public Result options(String s) {
		return ok();
	}
}
