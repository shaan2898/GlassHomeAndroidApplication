package com.example.shaan.hazardapp;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {

    private Button RegisterButton;
    private EditText CreateUserText;
    private EditText CreatePasswordText;
    private TextView backToMain;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        RegisterButton = (Button) findViewById(R.id.RegisterButton);
        CreatePasswordText = (EditText) findViewById(R.id.CreatePasswordText);
        CreateUserText = (EditText) findViewById(R.id.CreateUserText);
        backToMain = (TextView) findViewById(R.id.backToMain);
        firebaseAuth = FirebaseAuth.getInstance();

        //return to main page
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, MainActivity.class));
            }
        });



        //register user to Firebase when clicked, and redirect to Sign In page
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = CreateUserText.getText().toString().trim();
                String password = CreatePasswordText.getText().toString().trim();


                    boolean  EmailTest = isEmailValid(email);

//if email is empty or invalid
                if(TextUtils.isEmpty(email) || !EmailTest){
                    //email is empty
                    Toast.makeText(Registration.this, "Please enter a valid email", Toast.LENGTH_LONG).show();

                    //stop function from continuing until they enter email
                    return;
                }
                //if password is empty
                if(TextUtils.isEmpty(password) || password.length() < 6){
                    //password is empty
                    Toast.makeText(Registration.this, "Please enter a valid password, minimum 6 characters", Toast.LENGTH_LONG).show();

                    //stop function from continuing until they enter password
                    return;
                }

                //if both strings are valid
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    //user is successfully registered and logged in
                                    //will start the profile activity here
                                    //pop a message to show successfull registration
                                    Toast.makeText(Registration.this, "Registered Successfully.", Toast.LENGTH_SHORT).show();
                                    startActivity( new Intent(Registration.this, MainActivity.class));
                                }
                                else {
                                    Toast.makeText(Registration.this, "Registration Failed. Please try again", Toast.LENGTH_SHORT).show();
                                }

            }
        });


    }
});



    }
//check if user entered valid email address
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
