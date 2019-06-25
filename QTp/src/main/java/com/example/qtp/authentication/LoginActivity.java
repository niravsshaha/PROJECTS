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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.qtp.MainActivity;
import com.example.qtp.R;
import com.example.qtp.utils.Constants;
import com.example.qtp.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.ll_Login)
    LinearLayout layoutLogin;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static Intent getIntent(Context context, int flag) {
        Intent intent = new Intent(context, LoginActivity.class);

        if (flag > 0) {
            intent.setFlags(flag);
        }

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //firebaseLoginUI();

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        progressDialog = Utils.getProgressDialog(LoginActivity.this, "Signing in", "Please wait...");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            SharedPreferences.Editor spe = sp.edit();

            spe.putString(Constants.SP_KEY_DISPLAY_NAME, user.getDisplayName());
            spe.putString(Constants.SP_KEY_EMAIL, user.getEmail());
            spe.putString(Constants.SP_KEY_UID, user.getUid());

            if (user.getPhotoUrl() != null)
                spe.putString(Constants.SP_KEY_PHOTO_URL, user.getPhotoUrl().toString());

            spe.apply();

            startActivity(MainActivity.getIntent(LoginActivity.this, Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }




    @OnClick(R.id.tvSignUp)
    void onNewSignUp() {
        startActivity(CreateAccountActivity.getIntent(LoginActivity.this));
        finish();
    }

    @OnClick(R.id.bLogin)
    void onLoginClick() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        etEmail.setError(null);
        etPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            cancel = true;
            focusView = etEmail;
            etEmail.setError("Enter email");
        } else if (!Utils.isEmailValid(email)) {
            cancel = true;
            focusView = etEmail;
            etEmail.setError("Enter valid email");
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
            layoutLogin.setVisibility(View.GONE);
            progressDialog.show();
            attemptLogin(email, password);
        }
    }

    private void attemptLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    startActivity(MainActivity.getIntent(LoginActivity.this, Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    layoutLogin.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }
        });
    }
}
