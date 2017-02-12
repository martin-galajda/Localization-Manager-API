package services;

import model.Converter;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by martin on 12/12/16.
 */
public class ConverterService extends BaseDatabaseService<Converter> {
	public ConverterService()
	{
		super("converters", Converter.class);
	}

	public CompletionStage<List<Converter>> getConverters()
	{
		return this.fetchEntities();
	}

	public CompletionStage<Converter> addConverter(Converter converter)
	{
		return this.addEntity(converter);
	}

	public CompletionStage<Converter> updateConverter(Converter converter)
	{
		return this.updateEntity(converter);
	}

	public CompletionStage<Converter> getConverter(String entityId)
	{
		return this
				.getEntitiesEqualingTo("id", entityId)
				.thenApplyAsync(matchedConverters -> matchedConverters.size() == 1 ? matchedConverters.get(0) : null);
	}

	public CompletionStage<Boolean> deleteConverter(String converterId) {
		return this.deleteEntity(converterId);
	}

}
