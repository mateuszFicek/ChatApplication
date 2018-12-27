package com.example.mateusz.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Mateusz on 27.12.2018.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth firebaseAuth;
    EditText loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        firebaseAuth = FirebaseAuth.getInstance();
        loginEmail = (EditText) findViewById(R.id.emailTextLogin);
        loginPassword = (EditText) findViewById(R.id.passTextLogin);

        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.signUpText).setOnClickListener(this);

    }

    private void userLogin() {
        String emailStringLogin = loginEmail.getText().toString().trim();
        String passwordStringLogin = loginPassword.getText().toString().trim();

        if (emailStringLogin.isEmpty()) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordStringLogin.isEmpty()) {
            //password empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailStringLogin).matches()){
            loginEmail.setError("Please enter valid email address.");
            loginEmail.requestFocus();
            return;
        }

        if(passwordStringLogin.length() < 6){
            loginPassword.setError("Minimum 6 characters.");
            loginPassword.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(emailStringLogin, passwordStringLogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(Login.this, "Logged in succesfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Chat.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signInButton) {
            userLogin();
        }
        if(view.getId() == R.id.signUpText){
            //open register activity
            finish();
            startActivity(new Intent(this, Register.class));
        }
    }
}
