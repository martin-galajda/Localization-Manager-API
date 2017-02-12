package listeners;

import com.google.firebase.database.*;
import com.google.firebase.internal.NonNull;
import com.google.firebase.tasks.OnCompleteListener;
import com.google.firebase.tasks.OnSuccessListener;
import com.google.firebase.tasks.Task;
import model.BaseModelClass;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by martin on 12/8/16.
 */
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
