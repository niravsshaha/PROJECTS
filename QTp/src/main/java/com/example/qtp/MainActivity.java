package com.example.qtp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qtp.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.qtp.dialogs.AddMoneyDialogFragment;
import com.example.qtp.utils.Constants;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tvAvailableBalance)
    TextView tvAvailableBalance;

    @BindView(R.id.pbBalanceInMain)
    ProgressBar pbBalance;

    DatabaseReference userRef;
    ValueEventListener listener;

    public static Intent getIntent(Context context, int flag) {
        Intent intent = new Intent(context, MainActivity.class);

        if (flag > 0) {
            intent.setFlags(flag);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String uid = sp.getString(Constants.SP_KEY_UID, null);

        final String balanceText = getResources().getString(R.string.text_balance);


        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double availableAmount = dataSnapshot.getValue(Double.class);

                pbBalance.setVisibility(View.GONE);
                tvAvailableBalance.setText(String.format(balanceText, availableAmount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pbBalance.setVisibility(View.GONE);
                tvAvailableBalance.setText(R.string.text_balance_error);
            }
        };

        if (uid != null) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            userRef = database.getReference(Constants.USERS).child(uid).child(Constants.USER_INFO).child(Constants.USER_BALANCE);

            userRef.addValueEventListener(listener);
        } else {
            Toast.makeText(MainActivity.this, "Please login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(listener);
    }

    @OnClick(R.id.bMakePayment)
    void bMakePaymentClicked() {
        Intent intent = new Intent(MainActivity.this, QRCodeGeneratorActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.bScan)
    public void bScanClicked() {

        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.bAddAmount)
    void addAmount() {
        AddMoneyDialogFragment addMoneyDialogFragment = AddMoneyDialogFragment.newInstance();
        addMoneyDialogFragment.show(MainActivity.this.getFragmentManager(), "AddAmountDialogFragment");
    }
}
