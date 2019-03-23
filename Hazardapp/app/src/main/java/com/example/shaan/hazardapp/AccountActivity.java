package com.example.shaan.hazardapp;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;


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
    private static final String TAG = "AcccountActivity";
    private TextView DoorStatus;
    FirebaseAuth user = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootRef = firebaseDatabase.getReference();
   // private DatabaseReference mDoorRef = mRootRef.child();
    private String doorVal;
    //FirebaseAuth userId = FirebaseAuth.getCurrentUser().getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        DoorIDText = findViewById(R.id.DoorIDText);
        DoorStatus = findViewById(R.id.DoorStatus);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


mRootRef.child("message").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        doorVal = dataSnapshot.getValue().toString();
        DoorStatus.setText(doorVal);
       // Log.d("TAG", doorVal);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }

//    @Override
//    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        doorVal =
//         DoorStatus = dataSnapshot.getValue(String.class);
//
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//    }

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

            case R.id.sensorSettings:

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



    @Override
    protected void onStart() {
        super.onStart();
       // mDoorRef.addValueEventListener(this);

    }
}
