package listeners;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.database.*;
import model.BaseModelClass;
import model.Project;
import play.libs.Json;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by martin on 12/8/16.
 */
public class FirebaseDatabaseListener<T extends BaseModelClass> implements ValueEventListener {
	protected CompletableFuture<HashMap<String, T>> future;

	public FirebaseDatabaseListener(CompletableFuture<HashMap<String, T>> future) {
		this.future = future;
	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		GenericTypeIndicator<HashMap<String, T>> t = new GenericTypeIndicator<HashMap<String, T>>() {};
		HashMap<String, T> entities = dataSnapshot.getValue(t);
		entities.forEach((entityId, entityValue) -> {
			entityValue.setId(entityId);
		});
		future.complete(entities);
	}

	@Override
	public void onCancelled(DatabaseError databaseError) {
		throw new DatabaseException(databaseError.getMessage());
	}
}
