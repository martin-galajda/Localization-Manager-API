package services;

import model.User;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by martin on 12/8/16.
 */
@Singleton
public class UserService extends BaseDatabaseService<User> {
	public UserService() {
		super("users", User.class);
	}

	public CompletionStage<List<User>> getUsers() {
		return this.fetchEntities();
	}

	public CompletionStage<User> getUserByIdFromProvider(String idFromProvider) {
		//return this.getOneEntityEqualingTo("idFromProvider", idFromProvider);
		return this
				.getEntitiesEqualingTo("idFromProvider", idFromProvider)
				.thenApplyAsync(users -> {
					if (users.size() == 1) {
						return users.get(0);
					}
					return null;
				});
	}

	public CompletionStage<User> getUserById(String id) {
		//return this.getOneEntityEqualingTo("id", id);
		return this
				.getEntitiesEqualingTo("id", id)
				.thenApplyAsync(users -> {
					if (users.size() == 1) {
						return users.get(0);
					}
					return null;
				});
	}

	public CompletionStage<User> add(User user) {
		return this.addEntity(user);
	}

}
