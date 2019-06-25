package com.example.qtp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.qtp.R;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvUserName)
    public TextView tvUserName;

    @BindView(R.id.tvUserEmail)
    public TextView tvUserEmail;

    @BindView(R.id.tvTransactionAmount)
    public TextView tvTransactionAmount;

    @BindView(R.id.tvTime)
    public TextView tvTime;

    public TransactionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
