package com.example.qtp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import com.example.qtp.authentication.LoginActivity;
import com.example.qtp.dialogs.AddMoneyDialogFragment;
import com.example.qtp.utils.Constants;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        DrawerLayout drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);

        FrameLayout activityContainer = (FrameLayout) drawer.findViewById(R.id.activity_content);

        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        super.setContentView(drawer);


        Toolbar toolbar = (Toolbar) drawer.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) drawer.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setHeaderWithUserInfo(navigationView);
    }

    private void setHeaderWithUserInfo(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);

        TextView tvDisplayName = (TextView) headerView.findViewById(R.id.tvHeaderDisplayName);
        TextView tvEmail = (TextView) headerView.findViewById(R.id.tvHeaderEmail);
        CircleImageView ivDP = (CircleImageView) headerView.findViewById(R.id.ivHeaderDP);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);

        String displayName = "Hello " + sp.getString(Constants.SP_KEY_DISPLAY_NAME, "friend");
        String email = sp.getString(Constants.SP_KEY_EMAIL, "");
        String photoUrl = sp.getString(Constants.SP_KEY_PHOTO_URL, null);

        tvDisplayName.setText(displayName);
        tvEmail.setText(email);

        if (photoUrl != null)
            Picasso.with(BaseActivity.this).load(photoUrl).noFade().into(ivDP);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                if (!(this instanceof MainActivity))
                    startActivity(MainActivity.getIntent(BaseActivity.this, Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();

                startActivity(LoginActivity.getIntent(BaseActivity.this, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            case R.id.nav_add_amount:
                AddMoneyDialogFragment addMoneyDialogFragment = AddMoneyDialogFragment.newInstance();
                addMoneyDialogFragment.show(BaseActivity.this.getFragmentManager(), "AddAmountDialogFragment");
                break;
            case R.id.nav_transactions:
                if (!(this instanceof TransactionHistoryActivity))
                    startActivity(TransactionHistoryActivity.getIntent(BaseActivity.this, Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
