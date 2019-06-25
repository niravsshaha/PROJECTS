package com.example.qtp.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.qtp.MainActivity;
import com.example.qtp.R;
import com.example.qtp.models.User;
import com.example.qtp.utils.Constants;
import com.example.qtp.utils.Utils;

public class CreateAccountActivity extends AppCompatActivity {

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etMobile)
    EditText etMobile;

    @BindView(R.id.etNewEmail)
    EditText etEmail;

    @BindView(R.id.etNewMPIN)
    EditText etMPIN;

    @BindView(R.id.etNewPassword)
    EditText etPassword;

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    public static Intent getIntent(Context context) {
        return new Intent(context, CreateAccountActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        progressDialog = Utils.getProgressDialog(CreateAccountActivity.this, "Creating account", "Please wait...");

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.tvLogIn)
    void goToLogin() {
        startActivity(LoginActivity.getIntent(CreateAccountActivity.this, Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    @OnClick(R.id.bCreate)
    void CreateButtonClicked() {

        String name = etName.getText().toString();
        String contact = etMobile.getText().toString();
        String email = etEmail.getText().toString();
        String mpin = etMPIN.getText().toString();
        String password = etPassword.getText().toString();

        etName.setError(null);
        etMobile.setError(null);
        etEmail.setError(null);
        etMPIN.setError(null);
        etPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            cancel = true;
            focusView = etName;
            etName.setError("Required");
        } else if (TextUtils.isEmpty(contact)) {
            cancel = true;
            focusView = etMobile;
            etMobile.setError("Required");
        } else if (!Utils.isContactValid(contact)) {
            cancel = true;
            focusView = etMobile;
            etMobile.setError("10 digits");
        } else if (TextUtils.isEmpty(email)) {
            cancel = true;
            focusView = etEmail;
            etEmail.setError("Enter email");
        } else if (!Utils.isEmailValid(email)) {
            cancel = true;
            focusView = etEmail;
            etEmail.setError("Enter valid email");
        } else if (TextUtils.isEmpty(mpin)) {
            cancel = true;
            focusView = etMPIN;
            etMPIN.setError("Please enter MPIN");
        } else if (!Utils.isMpinValid(mpin)) {
            cancel = true;
            focusView = etMPIN;
            etMPIN.setError("MPIN should be 4 characters long");
        } else if (TextUtils.isEmpty(password)) {
            cancel = true;
            focusView = etPassword;
            etPassword.setError("Enter password");
        } else if (!Utils.isPasswordValid(password)) {
            cancel = true;
            focusView = etPassword;
            etPassword.setError("At least 8 characters required");
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mpin = Utils.encryptMPIN(mpin, Utils.getSalt());
            progressDialog.show();
            createNewAccount(name, contact, email, mpin, password);
        }
    }

    private void createNewAccount(final String name, final String contact, final String email, final String mpin, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = task.getResult().getUser();

                                    //if (user != null) {
                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(CreateAccountActivity.this);
                                    SharedPreferences.Editor spe = sp.edit();

                                    spe.putString(Constants.SP_KEY_DISPLAY_NAME, name);
                                    spe.putString(Constants.SP_KEY_EMAIL, user.getEmail());
                                    spe.putString(Constants.SP_KEY_UID, user.getUid());

                                    if (user.getPhotoUrl() != null)
                                        spe.putString(Constants.SP_KEY_PHOTO_URL, user.getPhotoUrl().toString());

                                    spe.apply();
                                    //}

                                    //update user profile with given name
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name).build();

                                    user.updateProfile(profileUpdates);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference userRef = database.getReference(Constants.USERS).child(user.getUid()).child(Constants.USER_INFO);

                                    User newUser = new User(name, contact, email, mpin);

                                    userRef.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                startActivity(MainActivity.getIntent(CreateAccountActivity.this, Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                finish();
                                            }
                                        }
                                    });

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateAccountActivity.this, "Network error. Try again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                );
    }
}
