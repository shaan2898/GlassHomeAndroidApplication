package com.example.shaan.hazardapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //declare variables to use
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button SignInButton;

    private TextView clickHere;
   // private TextView forgotPasswordText;


    //Declare Firebase Authentication to connect to Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        clickHere = (TextView) findViewById(R.id.clickHere);

        SignInButton = (Button) findViewById(R.id.SignInButton);
      //  forgotPasswordText = (Button) findViewById(R.id.forgotPasswordText);

        //Firebase Authentication, when entering username/password it will get current user and if found, take them to Account Activity

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){


                    startActivity(new Intent(MainActivity.this, AccountActivity.class));



                }

            }
        };

        //Stats the Sign in process when clicking "Sign In" button
        SignInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                startSignIn();


            }

        });

//click here to registration page
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registration.class));
            }
        });

//        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ResetPassword.class));
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener );

    }

    private void startSignIn() {

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();



        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this, "Fields are Empty.", Toast.LENGTH_LONG).show();
        }
        else{

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {



                    if(!task.isSuccessful()){

                        Toast.makeText(MainActivity.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();

                    }


                }
            });


        }


    }



}


