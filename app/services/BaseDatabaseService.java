package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import listeners.FirebaseDatabaseListener;
import model.BaseModelClass;
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
	String pathToEntity;

	BaseDatabaseService(String pathToEntity) {
		this.pathToEntity = pathToEntity;
	}

	protected DatabaseReference getReference(String path) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		return database.getReference(path);
	}


	protected CompletionStage<HashMap<String, T>> getEntitiesEqualingTo(String key, String value) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference(pathToEntity);
		CompletableFuture<HashMap<String, T>> promise = new CompletableFuture<>();

		if (key != null && value != null) {
			Query result = reference.orderByChild(key).equalTo(value);
			result.addListenerForSingleValueEvent(new FirebaseDatabaseListener<T>(promise));
		} else {
			reference.addListenerForSingleValueEvent(new FirebaseDatabaseListener<T>(promise));
		}

		return promise.thenApplyAsync(values -> values);
	}

	protected CompletionStage<HashMap<String, T>> fetchEntities() {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference reference = database.getReference(pathToEntity);
		CompletableFuture<HashMap<String, T>> promise = new CompletableFuture<>();

		reference.addListenerForSingleValueEvent(new FirebaseDatabaseListener<T>(promise));

		return promise.thenApplyAsync(values -> values);
	}
}
