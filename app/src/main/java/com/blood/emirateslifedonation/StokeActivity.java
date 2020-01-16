package com.blood.emirateslifedonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StokeActivity extends AppCompatActivity {

    private TextView aplsu, aminus, bplus, bminus;
    private TextView oplus, ominus, abplus, abminus;
    private DatabaseReference MuserDatabase;
    private Animation animation;

    private int ApositiveCounter,AnegtiveCounter,BpositiveCounter, BnegCounter, OpositiveCounter;
    private int OnegtiveCounter, ABpositiveCounter, ABnegtiveCounter;

    private DatabaseReference MdonarDatabase, MRequesterDatabase;

    private TextView totoaldonar, totoalreciver;

    private ImageView backbutton;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoke);

        backbutton = findViewById(R.id.BcakButtonID);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
               finish();
            }
        });

        totoaldonar = findViewById(R.id.TotalDonorID);
        totoalreciver = findViewById(R.id.TotalReciverID);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_button);
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        relativeLayout = findViewById(R.id.TopLayout);
        relativeLayout.setAnimation(animation);

        aplsu = findViewById(R.id.AplusText);
        aminus = findViewById(R.id.Aminustext);
        bplus = findViewById(R.id.bplustext);
        bminus = findViewById(R.id.bminustext);
        oplus = findViewById(R.id.oplustext);
        ominus = findViewById(R.id.ominustext);
        abplus = findViewById(R.id.abplustext);
        abminus = findViewById(R.id.abminustext);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        readingAllBloodGroup();

        MdonarDatabase = FirebaseDatabase.getInstance().getReference().child("DonarPost");
        MdonarDatabase.keepSynced(true);
        MRequesterDatabase = FirebaseDatabase.getInstance().getReference().child("Request_post");
        MRequesterDatabase.keepSynced(true);

        MdonarDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long donner =  dataSnapshot.getChildrenCount();
                totoaldonar.setText("Total Donors: "+donner);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        MRequesterDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long counter = dataSnapshot.getChildrenCount();
                totoalreciver.setText("Total Request: "+counter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void readingAllBloodGroup(){

        MuserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    String value = ds.getValue().toString();

                    if(value.equals("A+")){
                        ApositiveCounter++;
                        aplsu.setText("Total Bag "+ApositiveCounter);
                    }
                    if(value.equals("A-")){
                        AnegtiveCounter++;
                        aminus.setText("Total Bag "+AnegtiveCounter);
                    }
                    if(value.equals("B+")){
                        BpositiveCounter++;
                        bplus.setText("Total Bag "+BpositiveCounter);
                    }
                    if(value.equals("B-")){
                        BnegCounter++;
                        bminus.setText("Total Bag "+BnegCounter);
                    }
                    if(value.equals("O+")){
                        OpositiveCounter++;
                        oplus.setText("Total Bag "+OpositiveCounter);
                    }
                    if(value.equals("O-")){
                        OnegtiveCounter++;
                        ominus.setText("Total Bag "+OnegtiveCounter);
                    }
                    if(value.equals("AB+")){
                        ABpositiveCounter++;
                        abplus.setText("Total Bag "+ABpositiveCounter);
                    }
                    if(value.equals("AB-")){
                        ABnegtiveCounter++;
                        abminus.setText("Total Bag "+ABnegtiveCounter);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
