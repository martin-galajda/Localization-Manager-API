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
}
