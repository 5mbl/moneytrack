package com.app.moneytrack_newest;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private BarChart chart;
    private MaterialButton btnGoBack;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        chart = findViewById(R.id.chart);
        btnGoBack = findViewById(R.id.btn_go_back);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to the previous activity
            }
        });

        loadData();
    }

    private void loadData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            return;
        }

        String userEmail = user.getEmail();

        db.collection("Income")
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double totalIncome = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                String amountStr = document.getString("amount");
                                if (amountStr != null) {
                                    totalIncome += Double.parseDouble(amountStr);
                                }
                            }
                            loadSpendingData(totalIncome);
                        } else {
                            // Handle error
                        }
                    }
                });
    }

    private void loadSpendingData(final double totalIncome) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            return;
        }

        String userEmail = user.getEmail();

        db.collection("Spendings")
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double totalSpending = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                String amountStr = document.getString("amount");
                                if (amountStr != null) {
                                    totalSpending += Double.parseDouble(amountStr);
                                }
                            }
                            setData(totalIncome, totalSpending);
                        } else {
                            // Handle error
                        }
                    }
                });
    }

    private void setData(double totalIncome, double totalSpending) {
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0, (float) totalIncome, "Income"));
        values.add(new BarEntry(1, (float) totalSpending, "Spending"));

        BarDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Income vs Spending");
            set1.setDrawIcons(false);
            set1.setColors(new int[]{ContextCompat.getColor(this, android.R.color.holo_blue_light),
                    ContextCompat.getColor(this, android.R.color.holo_red_light)});
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            chart.setData(data);
        }

        chart.invalidate(); // Refresh the chart
    }
}