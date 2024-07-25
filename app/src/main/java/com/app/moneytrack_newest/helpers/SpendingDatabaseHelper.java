package com.app.moneytrack_newest.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app.moneytrack_newest.models.Spending;

import java.util.Map;

public class SpendingDatabaseHelper {
    private final CollectionReference collectionReference;

    public SpendingDatabaseHelper() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Spendings");
    }

    public Task<Void> addSpending(Spending spending) {
        return collectionReference.document(spending.getId()).set(spending);
    }

    // noch nicht implementiert in activity
    public Task<Void> updateSpending(String id, Map<String, Object> updates) {
        return collectionReference.document(id).update(updates);
    }

    // noch nicht implementiert in activity
    public Task<Void> deleteSpending(String id) {
        return collectionReference.document(id).delete();
    }

    public CollectionReference getSpendings() {
        return collectionReference;
    }
}