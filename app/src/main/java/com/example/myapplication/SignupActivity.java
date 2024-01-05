package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    Button signupButton,loginbtn;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName =findViewById(R.id.signup_name);
        signupEmail =findViewById(R.id.signup_email);
        signupUsername =findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        signupUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupUsername.setText("");
            }
        });

        signupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupName.setText("");
            }
        });
        signupPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupPassword.setText("");
            }
        });
        signupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupEmail.setText("");
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                database =FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String email =signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                HelperClass helperClass = new HelperClass(name,email,username,password);
                reference.child(name).setValue(helperClass);

                Intent intent789 = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent789);
            }
        });

        loginbtn = findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent789 = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent789);
            }
        });
    }

}