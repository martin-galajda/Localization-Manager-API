package services;

import play.Configuration;

import javax.inject.Inject;


public class ConfigService {
	@Inject
	Configuration configuration;

	public String[] getAdministratorEmailsFromConfig()
	{
		return this.configuration.getString("administrators").split(",");
	}
}
