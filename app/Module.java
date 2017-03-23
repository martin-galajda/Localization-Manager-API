import authentication.providers.GoogleProvider;
import com.google.inject.AbstractModule;
import services.ConfigService;
import services.ProjectService;
import services.UserService;

import java.time.Clock;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {

    @Override
    public void configure() {
        // Use the system clock as the default implementation of Clock
        bind(Clock.class).toInstance(Clock.systemDefaultZone());

        bind(ProjectService.class).asEagerSingleton();
        bind(UserService.class).asEagerSingleton();
        bind(GoogleProvider.class).asEagerSingleton();
        bind(FirebaseInitializer.class).asEagerSingleton();
		bind(ConfigService.class).asEagerSingleton();
    }

}
