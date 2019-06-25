package com.example.qtp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;

import com.example.qtp.R;
import com.example.qtp.models.Transaction;
import com.example.qtp.viewholders.TransactionViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class TransactionAdapter extends FirebaseRecyclerAdapter<Transaction, TransactionViewHolder> {
    private Context mContext;

    public TransactionAdapter(Context context, Class<Transaction> modelClass, int modelLayout, Class<TransactionViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = context;
    }

    public TransactionAdapter(Context context, Class<Transaction> transactionClass, int row_transaction, Class<TransactionViewHolder> transactionViewHolderClass, Query mSearchQuery) {
        super(transactionClass, row_transaction, transactionViewHolderClass, mSearchQuery);
        this.mContext = context;
    }

    @Override
    protected void populateViewHolder(TransactionViewHolder viewHolder, Transaction transaction, int position) {
        viewHolder.tvUserName.setText(transaction.getOtherUser());
        viewHolder.tvUserEmail.setText(transaction.getEmail());

        String relativeTime = String.valueOf(DateUtils.getRelativeTimeSpanString(Long.parseLong(transaction.getTimeStamp().toString())));

        viewHolder.tvTime.setText(relativeTime);

        String transactionAmount = mContext.getResources().getString(R.string.text_transaction_amount);

        if (transaction.isCredit()) {
            viewHolder.tvTransactionAmount.setText(String.format(transactionAmount, "+", transaction.getAmount()));
            viewHolder.tvTransactionAmount.setTextColor(Color.GREEN);
        } else {
            viewHolder.tvTransactionAmount.setText(String.format(transactionAmount, "-", transaction.getAmount()));
            viewHolder.tvTransactionAmount.setTextColor(Color.RED);
        }

    }
}
