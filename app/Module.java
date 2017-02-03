import authentication.providers.GoogleProvider;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.inject.AbstractModule;
import services.ProjectService;
import services.UserService;

import java.io.FileInputStream;
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
    public void configure(){
        // Use the system clock as the default implementation of Clock
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        // Ask Guice to create an instance of ApplicationTimer when the
        // application starts.
        bind(ProjectService.class).asEagerSingleton();
        bind(UserService.class).asEagerSingleton();
        bind(GoogleProvider.class).asEagerSingleton();

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setServiceAccount(new FileInputStream("./conf/LocaleManagementPrototype-99ff7f3630a2.json"))
                    .setDatabaseUrl("https://localemanagementprototype.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {

        }
    }

}
