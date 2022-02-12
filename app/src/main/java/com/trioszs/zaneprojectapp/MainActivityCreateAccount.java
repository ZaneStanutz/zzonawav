package com.trioszs.zaneprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivityCreateAccount extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    EditText passwordInputTwo;
    EditText phoneInput;
    EditText emailInput;
    CheckBox emailOk;
    Button createAccount;
    String phoneNumber;
    String userName;
    String password;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_create_account);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString("phoneNumber");
        }

        usernameInput = findViewById(R.id.editTextCreateAccountUsername);
        passwordInput = findViewById(R.id.editTextCreateAccountPassword);
        passwordInputTwo = findViewById(R.id.editTextCreateAccountPasswordTwo);
        phoneInput = findViewById(R.id.editTextCreateAccountPhoneNumber);
        phoneInput.setText(phoneNumber);
        emailInput = findViewById(R.id.editTextCreateAccountEmailAddress);
        emailOk = findViewById(R.id.checkBoxCreateAccountEmailOk);

        createAccount = findViewById(R.id.buttonCreateAccountSecondary);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usernameInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")
                    || passwordInputTwo.getText().toString().equals("") || phoneInput.getText().toString().equals("")
                    || emailInput.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
                else if(!passwordInput.getText().toString().equals(passwordInputTwo.getText().toString())) {

                    Toast.makeText(getApplicationContext(),"Password do not match", Toast.LENGTH_SHORT).show();
                }
                else if(usernameInput.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Username must be at least 6 characters", Toast.LENGTH_LONG).show();
                }
                else if(passwordInput.getText().toString().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                }

                else{

                    userName = usernameInput.getText().toString();
                    password = passwordInput.getText().toString();
                    Toast.makeText(getApplicationContext(),"Account Creation successful!", Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(getApplicationContext(), MainActivityVerifiedLogin.class);
                    login.putExtra("userName", userName);
                    login.putExtra("password", password);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(login);
                        }
                    },2000);


                }
            }
        }); // onclick

    } // on create
} // main