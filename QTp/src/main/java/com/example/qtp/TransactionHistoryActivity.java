package com.example.qtp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.qtp.adapters.TransactionAdapter;
import com.example.qtp.models.Transaction;
import com.example.qtp.utils.Constants;
import com.example.qtp.viewholders.TransactionViewHolder;

public class TransactionHistoryActivity extends BaseActivity {
    @BindView(R.id.recycler_view_all_transactions)
    RecyclerView rvAllTransactions;

    @BindView(R.id.tvBalanceText)
    TextView tvBalance;

    @BindView(R.id.pbBalanceInTransactionHistory)
    ProgressBar pbBalance;

    TransactionAdapter mAdapter;


    DatabaseReference mRef;
    Query mSearchQuery;
    DatabaseReference mUserRef;
    ValueEventListener mListener;
    FirebaseDatabase mDatabase;

    String uid;

    public static Intent getIntent(Context context, int flag) {
        Intent intent = new Intent(context, TransactionHistoryActivity.class);

        if (flag > 0) {
            intent.setFlags(flag);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        ButterKnife.bind(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        uid = sp.getString(Constants.SP_KEY_UID, null);

        updateBalance();
        initRecyclerView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUserRef.removeEventListener(mListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    MenuItem mSearchMenu;
    SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchMenu = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenu);
        search();

        MenuItemCompat.setOnActionExpandListener(mSearchMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                updateTransactionReference();
                if (mAdapter != null)
                    mAdapter.cleanup();
                mAdapter = new TransactionAdapter(TransactionHistoryActivity.this, Transaction.class, R.layout.row_transaction, TransactionViewHolder.class, mRef);
                rvAllTransactions.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    private void search() {

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchInput = query.toLowerCase();
                if (mAdapter != null)
                    mAdapter.cleanup();

                updateTransactionQuery(searchInput);
                mAdapter = new TransactionAdapter(TransactionHistoryActivity.this,Transaction.class,
                        R.layout.row_transaction, TransactionViewHolder.class, mSearchQuery);
                rvAllTransactions.setAdapter(mAdapter);
                if (mAdapter.getItemCount() <= 0) {
                    Toast.makeText(TransactionHistoryActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchInput = newText.toLowerCase();
                if (mAdapter != null)
                    mAdapter.cleanup();

                updateTransactionQuery(searchInput);
                mAdapter = new TransactionAdapter(TransactionHistoryActivity.this, Transaction.class,
                        R.layout.row_transaction, TransactionViewHolder.class, mSearchQuery);
                rvAllTransactions.setAdapter(mAdapter);
                return false;
            }
        });
    }

    private void updateBalance() {

        final String balanceText = getResources().getString(R.string.text_balance);

        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double availableAmount = dataSnapshot.getValue(Double.class);

                pbBalance.setVisibility(View.GONE);
                tvBalance.setText(String.format(balanceText, availableAmount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pbBalance.setVisibility(View.GONE);
                tvBalance.setText(R.string.text_balance_error);
            }
        };

        if (uid != null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            mUserRef = database.getReference(Constants.USERS).child(uid).child(Constants.USER_INFO).child(Constants.USER_BALANCE);

            mUserRef.addValueEventListener(mListener);
        } else {
            Toast.makeText(TransactionHistoryActivity.this, "Please login", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView() {
        mDatabase = FirebaseDatabase.getInstance();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvAllTransactions.setHasFixedSize(true);
        rvAllTransactions.setLayoutManager(mLayoutManager);

        updateTransactionReference();

        mAdapter = new TransactionAdapter(this, Transaction.class, R.layout.row_transaction, TransactionViewHolder.class, mRef);

        rvAllTransactions.setAdapter(mAdapter);
    }

    public void updateTransactionReference() {
        try {
            mRef = mDatabase.getReference(Constants.USERS).child(uid).child(Constants.USER_TRANSACTION);
            mRef.keepSynced(true);
        }catch (Exception e){
            Log.e("TransactionHistory", e.getMessage());
        }
    }

    public void updateTransactionQuery(String searchInput) {
        mSearchQuery = mDatabase.getReference(Constants.USERS).child(uid).child(Constants.USER_TRANSACTION).orderByChild("otherUser_lower").startAt(searchInput).endAt(searchInput + "~");
        mSearchQuery.keepSynced(true);
    }
}
