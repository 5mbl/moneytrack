package com.app.moneytrack_newest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.moneytrack_newest.models.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewTransactionsActivity extends AppCompatActivity {

    private static final String TAG = "ViewTransactionsActivity";

    private RecyclerView recyclerViewExpenses;
    private RecyclerView recyclerViewIncome;
    private TransactionAdapter expensesAdapter;
    private TransactionAdapter incomeAdapter;
    private List<Transaction> expensesList;
    private List<Transaction> incomeList;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions);

        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
        recyclerViewIncome = findViewById(R.id.recyclerViewIncome);
        Button buttonGoBack = findViewById(R.id.buttonGoBack);

        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIncome.setLayoutManager(new LinearLayoutManager(this));

        expensesList = new ArrayList<>();
        incomeList = new ArrayList<>();

        expensesAdapter = new TransactionAdapter(expensesList, true);
        incomeAdapter = new TransactionAdapter(incomeList, false);

        recyclerViewExpenses.setAdapter(expensesAdapter);
        recyclerViewIncome.setAdapter(incomeAdapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadTransactions();

        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTransactionsActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadTransactions() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Log.e(TAG, "User not authenticated");
            return;
        }

        String userEmail = user.getEmail();
        CollectionReference spendingsRef = db.collection("Spendings");
        CollectionReference incomeRef = db.collection("Income");

        // Load Spendings
        spendingsRef.whereEqualTo("userEmail", userEmail).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Transaction spending = document.toObject(Transaction.class);
                                spending.setType("Spending");
                                expensesList.add(spending);
                            }
                            expensesAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "Error getting spendings: ", task.getException());
                        }
                    }
                });

        // Load Income
        incomeRef.whereEqualTo("userEmail", userEmail).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Transaction income = document.toObject(Transaction.class);
                                income.setType("Income");
                                incomeList.add(income);
                            }
                            incomeAdapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "Error getting income: ", task.getException());
                        }
                    }
                });
    }
}