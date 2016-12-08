package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import listeners.FirebaseDatabaseListener;
import model.BaseModelClass;
import model.User;
import play.libs.F;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by martin on 12/8/16.
 */
public abstract class BaseDatabaseService<T extends BaseModelClass> {
	private final String pathToEntity;
	private final Class<T> genericEntity;

	BaseDatabaseService(String pathToEntity, Class<T> genericEntity) {
		this.pathToEntity = pathToEntity;
		this.genericEntity = genericEntity;
	}

	protected DatabaseReference getReference(String path) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		return database.getReference(path);
	}

	protected CompletableFuture<Collection<T>> getEntitiesEqualingTo(String key, String value) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference(pathToEntity);
		CompletableFuture<Collection<T>> promise = new CompletableFuture<>();

		if (key != null && value != null) {
			Query result = reference.orderByChild(key).equalTo(value);
			result.addListenerForSingleValueEvent(new FirebaseDatabaseListener<T>(promise, genericEntity));
		} else {
			reference.addListenerForSingleValueEvent(new FirebaseDatabaseListener<T>(promise, genericEntity));
		}

		return promise.thenApplyAsync(values -> values);
	}

	protected CompletableFuture<Collection<T>> fetchEntities() {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference(pathToEntity);
		CompletableFuture<Collection<T>> promise = new CompletableFuture<>();

		reference.addListenerForSingleValueEvent(new FirebaseDatabaseListener<T>(promise, genericEntity));

		return promise.thenApplyAsync(values -> values);
	}
}
