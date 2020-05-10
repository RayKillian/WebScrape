package com.test.webscrape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Compare extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener {

    static TextView editSearch;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String user_email;
    Boolean checkLogin;
    TextView navUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editSearch = findViewById(R.id.product_name);
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        editSearch.setOnClickListener(this);

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.user_label_email);
        if (firebaseUser != null){
            navUsername.setText(firebaseUser.getEmail());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){

            case R.id.nav_saved:
                Intent i = new Intent(Compare.this,SavedProducts.class);
                startActivity(i);
                break;

            case R.id.nav_credits:
                Intent iCdt = new Intent(Compare.this, Credits.class);
                startActivity(iCdt);
                break;

            case  R.id.nav_logout:
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    firebaseAuth.signOut();
                    checkLogin = false;
                    user_email = "user email ";
                    navUsername.setText(user_email);

                    Intent iLog = new Intent(Compare.this, MainActivity.class);
                    startActivity(iLog);
                    break;
                }
        }

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {

        if (v==editSearch){
            startActivity(new Intent(Compare.this,Search.class));
        }

    }
}
