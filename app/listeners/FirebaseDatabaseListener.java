package listeners;

import com.google.firebase.database.*;
import model.BaseModelClass;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by martin on 12/8/16.
 */
public class FirebaseDatabaseListener<T extends BaseModelClass> implements ValueEventListener {
	protected final CompletableFuture<Collection<T>> promise;
	private final Class<T> genericEntity;

	public FirebaseDatabaseListener(CompletableFuture<Collection<T>> future, Class<T> genericEntity) {
		this.promise = future;
		this.genericEntity = genericEntity;
	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		List<T> entitiesList = new ArrayList();
		for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
			T entity = childSnapshot.getValue(genericEntity);
			entity.setId(childSnapshot.getKey());
			entitiesList.add(entity);
		}

		promise.complete(entitiesList);
	}

	@Override
	public void onCancelled(DatabaseError databaseError) {
		throw new DatabaseException(databaseError.getMessage());
	}
}
