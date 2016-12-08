package services;

import model.User;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CompletionStage;

/**
 * Created by martin on 12/8/16.
 */
@Singleton
public class UserService extends BaseDatabaseService<User> {
	public UserService() {
		super("users", User.class);
	}

	public CompletionStage<Collection<User>> getUsers() {
		return this.fetchEntities().thenApplyAsync(HashMap::values);
	}
}
