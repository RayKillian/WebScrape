package com.test.webscrape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

public class SignUp extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button signup;
    TextView errorMessage;
    ProgressBar progressBar;
    Toolbar toolbar;
    private FirebaseAnalytics mFirebaseAnalytics;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    //String TAG = SignUp.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        email = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.signUpPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        signup = findViewById(R.id.signUpBtn);
        errorMessage = findViewById(R.id.errorSignUp);
        progressBar = findViewById(R.id.signUpProgress);
        toolbar = findViewById(R.id.signUpToolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Sign Up");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessage.setText(null);
                progressBar.setVisibility(View.VISIBLE);
                String eml = email.getText().toString();
                String pwd = password.getText().toString();
                String confirmPwd = confirmPassword.getText().toString();
                if (eml.isEmpty()){
                    errorMessage.setText("Please enter email id");
                    email.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else if (pwd.isEmpty()) {
                    errorMessage.setText("Please enter your password");
                    password.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else if (confirmPwd.isEmpty()) {
                    errorMessage.setText("Please re-enter your password");
                    confirmPassword.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else if (!pwd.equals(confirmPwd)){
                    errorMessage.setText("Confirm password must be the same with password");
                    confirmPassword.requestFocus();
                    password.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else if (eml.isEmpty() && pwd.isEmpty() && confirmPwd.isEmpty()){
                    Toast.makeText(SignUp.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } else if (!(eml.isEmpty() && pwd.isEmpty() && confirmPwd.isEmpty()) && pwd.equals(confirmPwd)){
                    firebaseAuth.createUserWithEmailAndPassword(eml, pwd)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
                            } else {
                                startActivity(new Intent(SignUp.this, MainActivity.class));
                                Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Error Occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
