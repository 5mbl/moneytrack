package com.app.moneytrack_newest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.moneytrack_newest.models.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditTransactionActivity extends AppCompatActivity {

    private static final String TAG = "EditTransactionActivity";

    private EditText editTextAmount, editTextDescription;
    private Button buttonUpdate, buttonGoBack;
    private FirebaseFirestore db;
    private String transactionId;
    private String transactionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonGoBack = findViewById(R.id.buttonGoBack); // Initialisiere den Go Back Button
        db = FirebaseFirestore.getInstance();

        transactionId = getIntent().getStringExtra("transactionId");
        transactionType = getIntent().getStringExtra("transactionType");

        if (transactionId == null || transactionType == null) {
            Toast.makeText(this, "No transaction ID or type provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Transaction ID: " + transactionId); // Debugging
        Log.d(TAG, "Transaction Type: " + transactionType); // Debugging

        loadTransactionData(transactionId);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTransaction(transactionId);
            }
        });

        buttonGoBack.setOnClickListener(new View.OnClickListener() { // Setze den OnClickListener für den Go Back Button
            @Override
            public void onClick(View v) {
                finish(); // Beendet die aktuelle Aktivität und geht zurück zur vorherigen Aktivität
            }
        });
    }

    private void loadTransactionData(String id) {
        // Überprüfe den Sammlungspfad basierend auf dem Transaktionstyp
        String collectionPath = transactionType.equals("Spending") ? "Spendings" : "Income";
        DocumentReference docRef = db.collection(collectionPath).document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Transaction transaction = document.toObject(Transaction.class);
                        editTextAmount.setText(transaction.getAmount());
                        editTextDescription.setText(transaction.getDescription());
                    } else {
                        Log.e(TAG, "No such document"); // Debugging
                        Toast.makeText(EditTransactionActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Load failed: " + task.getException()); // Debugging
                    Toast.makeText(EditTransactionActivity.this, "Load failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateTransaction(String id) {
        String amount = editTextAmount.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        Log.d(TAG, "Updating Transaction ID: " + id); // Debugging

        // Überprüfe den Sammlungspfad basierend auf dem Transaktionstyp
        String collectionPath = transactionType.equals("Spending") ? "Spendings" : "Income";
        DocumentReference docRef = db.collection(collectionPath).document(id);
        docRef.update("amount", amount, "description", description)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditTransactionActivity.this, "Transaction updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Update failed: " + e.getMessage()); // Debugging
                    Toast.makeText(EditTransactionActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
