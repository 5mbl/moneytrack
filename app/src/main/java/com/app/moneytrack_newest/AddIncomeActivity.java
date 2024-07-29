package com.app.moneytrack_newest;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddIncomeActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextDescription;
    private Spinner spinnerCategory;
    private Button buttonAddIncome, buttonGoBack;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addincome);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonAddIncome = findViewById(R.id.buttonAddIncome);
        buttonGoBack = findViewById(R.id.buttonGoBack);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.income_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        buttonAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome();
            }
        });

        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addIncome() {
        String amount = editTextAmount.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (amount.isEmpty() || category.equals(getString(R.string.select_category_prompt))) {
            Toast.makeText(this, "Amount and category are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = auth.getCurrentUser().getEmail();
        String id = db.collection("Income").document().getId();

        Map<String, Object> income = new HashMap<>();
        income.put("id", id);
        income.put("amount", amount);
        income.put("description", description);
        income.put("category", category);
        income.put("userEmail", userEmail);

        db.collection("Income").document(id).set(income)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddIncomeActivity.this, "Income added", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddIncomeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
