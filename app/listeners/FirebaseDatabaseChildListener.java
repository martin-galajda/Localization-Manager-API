package listeners;

import com.google.firebase.database.*;
import model.BaseModelClass;
import java.util.concurrent.CompletableFuture;

public class FirebaseDatabaseChildListener<T extends BaseModelClass>
		implements ChildEventListener
{
	protected final CompletableFuture<T> entityPromise;
	private final Class<T> genericEntityType;

	public FirebaseDatabaseChildListener(CompletableFuture<T> future, Class<T> genericEntity) {
		this.entityPromise = future;
		this.genericEntityType = genericEntity;
	}

	@Override
	public void onChildAdded(DataSnapshot dataSnapshot, String s) {
		if (dataSnapshot.exists()) {
			T entity = dataSnapshot.getValue(genericEntityType);
			entityPromise.complete(entity);
		}
		entityPromise.complete(null);
	}

	@Override
	public void onChildChanged(DataSnapshot dataSnapshot, String s) {
	}

	@Override
	public void onChildRemoved(DataSnapshot dataSnapshot) {
	}

	@Override
	public void onChildMoved(DataSnapshot dataSnapshot, String s) {
	}

	@Override
	public void onCancelled(DatabaseError databaseError) {
		System.err.println(databaseError);
		throw new RuntimeException(databaseError.getMessage());
	}
}
