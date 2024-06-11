package com.app.moneytrack_newest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button buttonAddSpending, buttonViewSpendings, buttonLogout, buttonAddIncome;
    TextView textViewUserDetails;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this); // Initialize Firebase
        setContentView(R.layout.activity_main_menu);

        auth = FirebaseAuth.getInstance();
        buttonAddSpending = findViewById(R.id.btn_add_spending);
        buttonViewSpendings = findViewById(R.id.btn_view_spendings);
        buttonLogout = findViewById(R.id.btn_logout);
        buttonAddIncome = findViewById(R.id.btn_add_income);
        textViewUserDetails = findViewById(R.id.user_details);

        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textViewUserDetails.setText(user.getEmail());
        }

        buttonAddSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, AddSpendingActivity.class);
                startActivity(intent);
            }
        });

        buttonAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, AddIncomeActivity.class);
                startActivity(intent);
            }
        });

        buttonViewSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ViewTransactionsActivity.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}