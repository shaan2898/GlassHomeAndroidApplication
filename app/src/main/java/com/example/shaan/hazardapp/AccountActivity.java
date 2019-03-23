package com.example.shaan.hazardapp;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class AccountActivity extends AppCompatActivity {

    private TextView DoorIDText;
    private static final String TAG = "AccountActivity";
    private TextView DoorStatus;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private TextView airDataText;
    private Switch lockMode;
    private TextView garageText;
    private TextView garageOpen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        firebaseAuth = FirebaseAuth.getInstance();
        DoorIDText = findViewById(R.id.DoorIDText);
        DoorStatus = (TextView) findViewById(R.id.textView8);
        airDataText = (TextView) findViewById(R.id.airDataText);
        garageText = (TextView) findViewById(R.id.garageText);
        garageOpen = (TextView) findViewById(R.id.garageOpen);

        //switch Lock Mode
        final Switch lockMode = (Switch) findViewById(R.id.lockSwitch);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //switch operations
        lockMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //toggle enabled
                    Toast.makeText(AccountActivity.this, "Lock Mode On", Toast.LENGTH_LONG).show();

                }
                else {
                    //toggle disabled
                    Toast.makeText(AccountActivity.this, "Lock Mode Off", Toast.LENGTH_LONG).show();

                }

                //Toast.makeText(AccountActivity.this, "Lock Mode Changed", Toast.LENGTH_LONG).show();
            }
        });


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
               // FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged:signed-in:" + user.getUid());
                    //Toast.makeText(AccountActivity.this,"Successfully Logged in with " + user.getEmail(),Toast.LENGTH_SHORT).show();
                } else {
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged:signed-out");
                    //Toast.makeText(AccountActivity.this,"Successfully Logged out ",Toast.LENGTH_SHORT).show();

                }
            }
        };



        //firebase database declarations
//        mAuth = FirebaseAuth.getInstance();
//       // mFirebaseDatabase = FirebaseDatabase.getInstance();
//       // myRef = mFirebaseDatabase.getReference();
//        FirebaseUser user = mAuth.getCurrentUser();
//        userID = user.getUid();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        ///new vid

//        trueB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                FirebaseDatabase database = FirebaseDatabase.getInstance();
////                DatabaseReference myRef = database.getReference().child("sensors").child("dooropen").addValueEventListener();
////
////
////                myRef.setValue("true");
//            }
//        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("sensors");



    //for dooropen realtime data DONT CHANGE
        myRef.child("dooropen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                String str = String.valueOf(value);

                DoorStatus = (TextView) findViewById(R.id.textView8);

                if(str == "true" || str == "True" ){
                    DoorStatus.setText("Open");
                }
                if(str == "false" || str == "False" ){
                    DoorStatus.setText("Closed");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //for airQuality realtime data DONT CHANGE
        myRef.child("airquality").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value1 = dataSnapshot.getValue().toString();
                Integer val = Integer.valueOf(value1);

                airDataText = (TextView) findViewById(R.id.airDataText);


                switch(val){
                    case 0:
                        airDataText.setText("0");
                        break;
                    case 1:
                        airDataText.setText("1");
                        break;
                    case 2:
                        airDataText.setText("2");
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        //Garage Door REALTIME
        myRef.child("garageopen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value2 = dataSnapshot.getValue().toString();
                String str2 = String.valueOf(value2);

                garageText = (TextView) findViewById(R.id.garageText);

                if(str2 == "true" || str2 == "True" ){
                    garageText.setText("Open");
                }
                if(str2 == "false" || str2 == "False" ){
                    garageText.setText("Closed");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });





    }



    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener );


    }

    @Override
    public void onStop() {
        super.onStop();
        if(firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }


    //tab options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuLogout:

               FirebaseAuth.getInstance().signOut();
               finish();
               startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.Settings:

                startActivity(new Intent(this,SettingsActivity.class ));
                break;

            case R.id.map:

                startActivity(new Intent(this,MapActivity.class ));
                break;
            case R.id.AccountSettings:

                startActivity(new Intent(this,AccountSettings.class ));
                break;
        }


        return true;
    }

}
