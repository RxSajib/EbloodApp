package com.blood.emirateslifedonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private ProgressDialog Mprogress;
    private FirebaseAuth Mauth;
    private DatabaseReference Muserdatabase;
    private String CurrentUserID;
    private CircleImageView profileimage;
    private TextView username, emailaddress, bloodgroup;

    private EditText inputname, inputemail;
    private Button updatebutton;

    private ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        updatebutton = findViewById(R.id.UpdateProfile);
        inputname = findViewById(R.id.ProfileUserNameID);
        inputemail = findViewById(R.id.ProfileEmailID);

        backbutton = findViewById(R.id.ProfileBackButtonID);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileimage = findViewById(R.id.EditProfileImageID);
        username = findViewById(R.id.EditNameID);
        emailaddress = findViewById(R.id.EmailAdID);
        bloodgroup = findViewById(R.id.Blood);
        Mprogress = new ProgressDialog(EditProfileActivity.this);


        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        Muserdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Muserdatabase.keepSynced(true);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        readingyuseringfo();

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingupdatedata();
            }
        });
    }

    private void startingupdatedata(){
        String emailtext = inputemail.getText().toString();
        String usernametext = inputname.getText().toString();

        if(emailtext.isEmpty()){
            inputemail.setError("Email require");
        }
        else if(usernametext.isEmpty()){
            inputname.setError("Name require");
        }
        else {

            Mprogress.setTitle("Please wait");
            Mprogress.setMessage("We are updating your data");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            Map usermap = new HashMap();
            usermap.put("email", emailtext);
            usermap.put("Name", usernametext);

            Muserdatabase.child(CurrentUserID).updateChildren(usermap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Mprogress.dismiss();
                                inputemail.setText(null);
                                inputname.setText(null);
                                Toast.makeText(getApplicationContext(), "data is update", Toast.LENGTH_LONG).show();
                                readingyuseringfo();
                            }
                            else {
                                Mprogress.dismiss();
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Mprogress.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void readingyuseringfo(){
        Muserdatabase.child(CurrentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("imageurl")){
                                String imageurlget = dataSnapshot.child("imageurl").getValue().toString();
                                Picasso.with(getApplicationContext()).load(imageurlget).placeholder(R.drawable.defaultimage).into(profileimage);

                                Picasso.with(getApplicationContext()).load(imageurlget).networkPolicy(NetworkPolicy.OFFLINE).into(profileimage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                            }
                            if(dataSnapshot.hasChild("Name")){
                                String Nameget = dataSnapshot.child("Name").getValue().toString();
                                username.setText(Nameget);
                            }
                            if(dataSnapshot.hasChild("blood")){
                                String bloodget = dataSnapshot.child("blood").getValue().toString();
                                bloodgroup.setText(bloodget);
                            }
                            if(dataSnapshot.hasChild("email")){
                                String emailget = dataSnapshot.child("email").getValue().toString();
                                emailaddress.setText(emailget);
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "No user found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
