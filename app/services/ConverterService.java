package services;

import model.Converter;

import java.util.Collection;
import java.util.concurrent.CompletionStage;

/**
 * Created by martin on 12/12/16.
 */
public class ConverterService extends BaseDatabaseService<Converter> {
	public ConverterService()
	{
		super("converters", Converter.class);
	}

	public CompletionStage<Collection<Converter>> getConverters()
	{
		return this.fetchEntities();
	}
}
