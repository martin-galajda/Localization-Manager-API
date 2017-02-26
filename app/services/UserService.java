package services;

import model.User;
import play.libs.concurrent.HttpExecutionContext;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
		return this.getOneEntityEqualingTo("idFromProvider", idFromProvider);
	}

	public CompletionStage<User> getUserById(String id) {
		return this.getOneEntityEqualingTo("id", id);
	}

	public CompletionStage<User> add(User user) {
		return this.addEntity(user);
	}

	public CompletionStage<User> updateUser(User user) {
		return this.updateEntity(user);
	}

	public CompletionStage<User> updateUserAssignability(String userId, Boolean isAssignable) {
		return this.getUserById(userId).thenApplyAsync(user -> {
			if (user == null) {
				return null;
			}

			user.setIsAssignable(isAssignable);

			this.updateEntity(user);
			return user;
		});
	}

}
