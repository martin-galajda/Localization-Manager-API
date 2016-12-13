package controllers;

import play.libs.Json;
import play.mvc.Result;
import services.ConverterService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

/**
 * Created by martin on 12/12/16.
 */
public class ConverterController {

	@Inject
	ConverterService converterService;

	public CompletionStage<Result> getConverters() {
		return this.converterService.getConverters().thenApplyAsync(converters -> ok(Json.toJson(converters)));
	}
}
