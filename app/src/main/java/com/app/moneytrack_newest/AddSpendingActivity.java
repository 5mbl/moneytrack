package com.app.moneytrack_newest;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.moneytrack_newest.helpers.SpendingDatabaseHelper;
import com.app.moneytrack_newest.models.Spending;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddSpendingActivity extends AppCompatActivity {

    private static final String TAG = "AddSpendingActivity";

    EditText editTextAmount, editTextDescription;
    Button buttonSave;

    SpendingDatabaseHelper spendingDatabaseHelper;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addspending);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);

        spendingDatabaseHelper = new SpendingDatabaseHelper();
        auth = FirebaseAuth.getInstance();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSpending();
            }
        });
    }

    private void saveSpending() {
        String amount = editTextAmount.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (TextUtils.isEmpty(amount)) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = user.getEmail();
        String id = spendingDatabaseHelper.getSpendings().document().getId();
        if (id == null) {
            Toast.makeText(this, "Failed to generate ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Spending spending = new Spending(id, amount, description, userEmail);

        spendingDatabaseHelper.addSpending(spending).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddSpendingActivity.this, "Spending saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e(TAG, "Error saving spending", task.getException());
                    Toast.makeText(AddSpendingActivity.this, "Failed to save spending", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}