package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.Converter;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ConverterService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

/**
 * Created by martin on 12/12/16.
 */
public class ConverterController extends Controller {

	@Inject
	ConverterService converterService;

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
		JsonNode newConverterJson = request().body().asJson();
		System.err.println("New converter json: " + newConverterJson);
		Converter newConverter = Converter.create(newConverterJson);

		return this.converterService
				.addConverter(newConverter)
				.thenApplyAsync(converter -> ok(Json.toJson(converter)));
	}

	public CompletionStage<Result> postConverterUpdate(String entityId) {
		JsonNode newConverterJson = request().body().asJson();
		System.err.println("New converter json: " + newConverterJson);
		Converter newConverter = Converter.create(newConverterJson);

		return this.converterService
				.updateConverter(newConverter)
				.thenApplyAsync(converter -> ok(Json.toJson(converter)));
	}
}
