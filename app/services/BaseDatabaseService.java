package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.*;
import listeners.FirebaseDatabaseChildListener;
import listeners.FirebaseDatabaseListener;
import model.BaseModelClass;
import model.User;
import play.libs.F;

import java.util.*;
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

	protected CompletableFuture<List<T>> getEntitiesEqualingTo(String key, String value) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference(pathToEntity);
		CompletableFuture<List<T>> promise = new CompletableFuture<>();

		if (key != null && value != null) {
			Query result = reference.orderByChild(key).equalTo(value);
			result.addListenerForSingleValueEvent(new FirebaseDatabaseListener<T>(promise, genericEntity));
		} else {
			reference.addListenerForSingleValueEvent(new FirebaseDatabaseListener<T>(promise, genericEntity));
		}

		return promise.thenApplyAsync(values -> values);
	}

	protected CompletableFuture<T> getOneEntityEqualingTo(String key, String value) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference(pathToEntity);
		CompletableFuture<T> promise = new CompletableFuture<>();
		System.err.println("Inside get One Entity Equaling to");

		if (key != null && value != null) {
			System.err.println("key" + key);
			System.err.println("value" + value);
			Query result = reference.orderByChild(key).equalTo(value);
			result.addChildEventListener(new FirebaseDatabaseChildListener<>(promise, genericEntity));
		}

		return promise.thenApplyAsync(entityValue -> entityValue);
	}

	protected CompletableFuture<List<T>> fetchEntities() {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference(pathToEntity);
		CompletableFuture<List<T>> promise = new CompletableFuture<>();

		reference.addListenerForSingleValueEvent(new FirebaseDatabaseListener<T>(promise, genericEntity));

		return promise.thenApplyAsync(values -> values);
	}

	protected CompletableFuture<T> addEntity(T entity) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference(pathToEntity);
		CompletableFuture<T> promise = new CompletableFuture<>();

		DatabaseReference entityReference = reference.push();
		entity.setId(entityReference.getKey());
		entityReference
				.setValue(entity)
				.addOnCompleteListener(new FirebaseDatabaseListener<T>(promise, genericEntity, entity));

		return promise;
	}

	protected CompletableFuture<T> updateEntity(T entity) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference(pathToEntity);
		CompletableFuture<T> promise = new CompletableFuture<>();

		Map<String, Object> entityUpdates = new HashMap<>();
		entityUpdates.put(entity.getId(), entity);
		reference.updateChildren(entityUpdates, new FirebaseDatabaseListener<T>(promise, genericEntity, entity));

		return promise;
	}
}
