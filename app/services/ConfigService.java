package services;

import play.Configuration;

import javax.inject.Inject;

/**
 * Provides logic tied to configuring the application.
 */
public class ConfigService {
	@Inject
	Configuration configuration;

	public String[] getAdministratorEmailsFromConfig()
	{
		return this.configuration.getString("administrators").split(",");
	}

	public String getSecretAuthorizationToken()
	{
		return this.configuration.getString("secret.token");
	}

	public String getBackendServerUrl()
	{
		return this.configuration.getString("backendUrl");
	}

	public String getFrontendServerUrl()
    {
        return this.configuration.getString("http.client.address");
    }

}
