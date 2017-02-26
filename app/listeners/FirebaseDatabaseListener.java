package listeners;

import com.google.firebase.database.*;
import com.google.firebase.internal.NonNull;
import com.google.firebase.tasks.OnCompleteListener;
import com.google.firebase.tasks.OnSuccessListener;
import com.google.firebase.tasks.Task;
import model.BaseModelClass;

import java.util.*;
import java.util.concurrent.CompletableFuture;


public class FirebaseDatabaseListener<T extends BaseModelClass>
		implements ValueEventListener,
		OnCompleteListener<Void>,
		OnSuccessListener<Void>,
		DatabaseReference.CompletionListener
{
	protected final CompletableFuture<List<T>> collectionPromise;
	protected final CompletableFuture<T> entityPromise;
	private final Class<T> genericEntityType;
	private final T entity;

	@Override
	public void onSuccess(Void aVoid) {
		entityPromise.complete(entity);
	}

	public FirebaseDatabaseListener(CompletableFuture<List<T>> future, Class<T> genericEntity) {
		this.collectionPromise = future;
		this.genericEntityType = genericEntity;
		entityPromise = null;
		entity = null;
	}

	public FirebaseDatabaseListener(CompletableFuture<T> future, Class<T> genericEntity, T entity) {
		this.entityPromise = future;
		this.genericEntityType = genericEntity;
		this.entity = entity;
		collectionPromise = null;
	}

	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		ArrayList<T> entitiesList = new ArrayList<>();
		for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
			T entity = childSnapshot.getValue(genericEntityType);
			entity.setId(childSnapshot.getKey());
			entitiesList.add(entity);
		}

		collectionPromise.complete(entitiesList);
	}

	@Override
	public void onCancelled(DatabaseError databaseError) {
		throw new DatabaseException(databaseError.getMessage());
	}

	@Override
	public void onComplete(@NonNull Task<Void> task) {
		task.addOnSuccessListener(this);
	}

	@Override
	public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
		entityPromise.complete(entity);
	}
}
