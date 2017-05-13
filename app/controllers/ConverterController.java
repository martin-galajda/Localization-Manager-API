package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.Converter;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ConverterService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

/**
 * Created by martin on 12/12/16.
 */
public class ConverterController extends Controller {

	@Inject
	ConverterService converterService;

	@Inject
	FormFactory formFactory;

	public CompletionStage<Result> getConverters() {
		return this.converterService.getConverters().thenApplyAsync(converters -> ok(Json.toJson(converters)));
	}

	public CompletionStage<Result> getConverter(String id) {
		return this
				.converterService
				.getConverter(id)
				.thenApplyAsync(converter -> ok(Json.toJson(converter)));
	}

	public CompletionStage<Result> deleteConverter(String id) {
		return this
				.converterService
				.deleteConverter(id)
				.thenApplyAsync(deleted -> ok());
	}

	public CompletionStage<Result> postConverter() {
		Form<Converter> form = formFactory.form(Converter.class).bindFromRequest();

		if (form.hasErrors()) {
			CompletableFuture<Result> badRequestPromise = new CompletableFuture<>();
			badRequestPromise.complete(badRequest(form.errorsAsJson()));
			return badRequestPromise;
		}

		Converter newConverter = form.get();

		return this.converterService
				.addConverter(newConverter)
				.thenApplyAsync(converter -> ok(Json.toJson(converter)));
	}

	public CompletionStage<Result> postConverterUpdate(String entityId) {
		JsonNode newConverterJson = request().body().asJson();
		Converter newConverter = Converter.create(newConverterJson);

		return this.converterService
				.updateConverter(newConverter)
				.thenApplyAsync(converter -> ok(Json.toJson(converter)));
	}
}
