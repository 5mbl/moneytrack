package com.app.moneytrack_newest.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app.moneytrack_newest.models.Income;

import java.util.Map;

public class IncomeDatabaseHelper {
    private CollectionReference collectionReference;

    public IncomeDatabaseHelper() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Income");
    }

    public Task<Void> addIncome(Income income) {
        return collectionReference.document(income.getId()).set(income);
    }

    public Task<Void> updateIncome(String id, Map<String, Object> updates) {
        return collectionReference.document(id).update(updates);
    }

    public Task<Void> deleteIncome(String id) {
        return collectionReference.document(id).delete();
    }

    public CollectionReference getIncome() {
        return collectionReference;
    }
}