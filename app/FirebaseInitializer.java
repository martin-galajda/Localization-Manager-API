import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import play.Configuration;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by martin on 2/15/17.
 */
public class FirebaseInitializer {
	private Configuration configuration;

	@Inject
	public FirebaseInitializer(Configuration configuration) {
		this.configuration = configuration;
		this.initialize();
	}

	public void initialize() {
		try {
			String databaseUrl = this.configuration.getString("firebase.databaseUrl");
			String pathToServiceAccount = this.configuration.getString("firebase.pathToServiceAccount");

			if (databaseUrl == null) {
				throw new RuntimeException("firebase.databaseUrl in configuration was not provided!");
			}

			if (pathToServiceAccount == null) {
				throw new RuntimeException("firebase.pathToServiceAccount in configuration was not provided!");
			}

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setServiceAccount(new FileInputStream(pathToServiceAccount))
					.setDatabaseUrl(databaseUrl)
					.build();
			FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Failed to open file: ", e);
		}
	}
}
