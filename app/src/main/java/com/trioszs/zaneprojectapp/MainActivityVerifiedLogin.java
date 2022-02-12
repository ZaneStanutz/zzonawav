package com.trioszs.zaneprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityVerifiedLogin extends AppCompatActivity {

    String phoneNumber;
    String userNameVerified;
    String passwordVerified;

    boolean accCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_verified_login);

        EditText username = findViewById(R.id.editTextUsernameLogin);
        EditText password = findViewById(R.id.editTextTextPassword);
        TextView loginFailed = findViewById(R.id.textViewLoginFailed);
        loginFailed.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras.getString("userPhone") != null) {
            phoneNumber = extras.getString("userPhone");
        }
        if (extras.getString("userName") != null) {
            accCreated = true;
            userNameVerified = extras.getString("userName");
            passwordVerified = extras.getString("password");
            username.setText(userNameVerified);
            password.setText(passwordVerified);

        }

        Button userLogin = findViewById(R.id.buttonLoginUser);
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().toString().length() < 6 || password.getText().toString().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Please enter valid username and password", Toast.LENGTH_LONG).show();

                }
                else{
                    Intent downloadPage = new Intent(getApplicationContext(), MainActivityDownloadMusic.class);
                    startActivity(downloadPage);

                }
            }
        });

        Button createAccount = findViewById(R.id.buttonCreateAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createAccount = new Intent(getApplicationContext(), MainActivityCreateAccount.class);
                createAccount.putExtra("phoneNumber", phoneNumber);
                startActivity(createAccount);

            }
        });

        if(accCreated){
            createAccount.setEnabled(false);
        }

    }

}
