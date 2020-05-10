package com.test.webscrape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    Button signUpButton;
    EditText emailAddress;
    EditText password;
    ProgressBar progressBar;
    TextView errorMessage;
    //String TAG = MainActivity.class.getSimpleName();

    //private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        loginButton = findViewById(R.id.login_btn);
        signUpButton = findViewById(R.id.sign_up);
        emailAddress = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        progressBar = findViewById(R.id.progress_login_bar);
        errorMessage = findViewById(R.id.Error_message);
        Toolbar toolbar = findViewById(R.id.Login_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Login");

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, Compare.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eml = emailAddress.getText().toString();
                String pwd = password.getText().toString();
                errorMessage.setText(null);
                progressBar.setVisibility(View.VISIBLE);
                if (eml.isEmpty()){
                    errorMessage.setText("Please enter email id");
                    emailAddress.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else if (pwd.isEmpty()) {
                    errorMessage.setText("Please enter your password");
                    password.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }else if (eml.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } else if (!(eml.isEmpty() && pwd.isEmpty())){
                    mAuth.signInWithEmailAndPassword(eml, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                            } else {
                                Intent compare = new Intent(MainActivity.this, Compare.class);
                                startActivity(compare);
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Error Occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });
    }
}
