package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class AppController extends Controller {
	public Result options() {
		return options("/");
	}

	public Result options(String s) {
		return ok();
	}
}
