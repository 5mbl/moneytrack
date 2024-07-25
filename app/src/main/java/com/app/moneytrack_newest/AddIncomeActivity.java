package com.app.moneytrack_newest;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.moneytrack_newest.models.Income;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddIncomeActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextDescription;
    private Spinner spinnerCategory;
    private Button buttonAddIncome, buttonGoBack;
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
        db = FirebaseFirestore.getInstance();

        // Initialisiere den Spinner mit Kategorien und einem Platzhalter
        List<String> categories = new ArrayList<>();
        categories.add("Kategorie auswählen");
        categories.add("Gehalt");
        categories.add("Sonstiges");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
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
                finish(); // Beendet die aktuelle Aktivität und geht zurück zur vorherigen Aktivität
            }
        });
    }

    private void addIncome() {
        String amount = editTextAmount.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (amount.isEmpty() || description.isEmpty() || category.equals("Kategorie auswählen")) {
            Toast.makeText(this, "Bitte alle Felder ausfüllen und eine Kategorie auswählen", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = db.collection("Income").document().getId();
        Income income = new Income(id, amount, description, userEmail, category);

        db.collection("Income").document(id).set(income)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddIncomeActivity.this, "Income added", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddIncomeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
