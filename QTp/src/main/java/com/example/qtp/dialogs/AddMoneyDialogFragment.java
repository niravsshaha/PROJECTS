package com.example.qtp.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.qtp.R;
import com.example.qtp.models.Transaction;
import com.example.qtp.utils.Constants;
import com.example.qtp.utils.Utils;

/**
 * Adds a money to account
 */
public class AddMoneyDialogFragment extends DialogFragment {
    EditText etAddMoney;

    ProgressDialog progressDialog;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static AddMoneyDialogFragment newInstance() {
        return new AddMoneyDialogFragment();
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_money, null);
        etAddMoney = (EditText) rootView.findViewById(R.id.et_amount_to_add);

        progressDialog = Utils.getProgressDialog(getActivity(), "Updating balance", "Please wait...");

        /**
         * Call addMoney() when user taps "Done" keyboard action
         */
        etAddMoney.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    addMoney();
                }
                return true;
            }
        });

        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout*/
        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(R.string.positive_button_add_amount, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //addMoney();
                    }
                });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do something
                        addMoney();
                    }
                });
            }
        });

        //return builder.create();
        return alert;
    }

    /**
     * Post new request
     */
    public void addMoney() {

        etAddMoney.setError(null);

        final String userEnteredAmount = etAddMoney.getText().toString();

        boolean cancel = false;
        View focusView = null;


        /**
         * If EditText input is not empty
         */
        if (TextUtils.isEmpty(userEnteredAmount)) {
            etAddMoney.setError(getString(R.string.error_field_required));
            focusView = etAddMoney;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt posting and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            progressDialog.show();


            /* Get user data from sp*/
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String uid = sp.getString(Constants.SP_KEY_UID, null);
            final String name = sp.getString(Constants.SP_KEY_DISPLAY_NAME, null);
            final String email = sp.getString(Constants.SP_KEY_EMAIL, null);

            final double newAmount = Double.parseDouble(userEnteredAmount);

            /**
             * Create Firebase references
             */

            final DatabaseReference transactionRef = FirebaseDatabase.getInstance().getReference(Constants.USERS).child(uid).child(Constants.USER_TRANSACTION);
            final DatabaseReference amountRef = FirebaseDatabase.getInstance().getReference(Constants.USERS).child(uid).child(Constants.USER_INFO).child(Constants.USER_BALANCE);
            amountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final double finalAmount = newAmount + dataSnapshot.getValue(Double.class);
                    amountRef.setValue(finalAmount, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {

                                //Update transaction node
                                Transaction transaction = new Transaction(name, email, newAmount, true);
                                transactionRef.push().setValue(transaction);

                                /* Close the dialog fragment */
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Amount added successfully!", Toast.LENGTH_SHORT).show();
                                            /* Close the dialog fragment */
                                AddMoneyDialogFragment.this.getDialog().cancel();
                            } else {
                                /* Close the dialog fragment */
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Failed! Try again...", Toast.LENGTH_SHORT).show();
                                            /* Close the dialog fragment */
                                AddMoneyDialogFragment.this.getDialog().cancel();
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}

