package listeners;

import com.google.firebase.database.*;
import model.BaseModelClass;

import java.util.concurrent.CompletableFuture;


public class FirebaseDatabaseUpdateListener<T extends BaseModelClass>
		implements DatabaseReference.CompletionListener
{
	protected final CompletableFuture<T> promise;
	private final Class<T> genericEntityType;
	private final T entity;

	public FirebaseDatabaseUpdateListener(CompletableFuture<T> future, Class<T> genericEntity) {
		this.promise = future;
		this.genericEntityType = genericEntity;
		entity = null;
	}

	@Override
	public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
		promise.complete(entity);
	}
}
