package com.app.moneytrack_newest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.moneytrack_newest.models.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<Transaction> transactionList;
    private final boolean isExpense;
    private final ViewTransactionsActivity parentActivity;

    public TransactionAdapter(List<Transaction> transactionList, boolean isExpense, ViewTransactionsActivity parentActivity) {
        this.transactionList = transactionList;
        this.isExpense = isExpense;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.textViewAmount.setText(transaction.getAmount());
        holder.textViewType.setText(transaction.getType());
        holder.textViewCategory.setText(transaction.getCategory());

        if (isExpense) {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.expense_background));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.income_background));
        }

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.startEditTransactionActivity(transaction.getId(), transaction.getType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAmount, textViewType, textViewCategory;
        Button buttonEdit;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewType = itemView.findViewById(R.id.textViewType);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}
