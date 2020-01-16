package com.blood.emirateslifedonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner agespinner;
    private String[] ageArray = {"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44",
            "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66", "67", "68", "69", "70"};

    private EditText name, mobilenumber, location, post;
    private RelativeLayout femailbutton, malebutton;
    private ImageView maleicon, femailicon;
    private long countpost = 0;

    private RelativeLayout aplusbutton, aminusbutton, bplusbutton, bminusbutton, oplusbutton, ominusbutton;
    private RelativeLayout abplusbutton, abminusbutton;

    private ImageView aplusicon, aminusicon, bplusicon, bminusicon, oplusicon, ominusicon;
    private ImageView abplusicon, abminusicon;

    private CircleImageView profileimage;
    private Uri profileimageuri;
    private String Downloaduri;
    private FirebaseAuth Mauth;
    private String CurrentUserID;
    private StorageReference Mprofilestores;
    private String AgeString="";
    private String Gendertext="";
    private String Bloodtext = "";
    private String CurrentTime, CurrentDate;
    private DatabaseReference DonarpostRef;
    private ProgressDialog Mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        Mprogress = new ProgressDialog(AddPostActivity.this);
        profileimage = findViewById(R.id.DonarPostprofileImageID);
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        Mprofilestores = FirebaseStorage.getInstance().getReference();

        DonarpostRef = FirebaseDatabase.getInstance().getReference().child("DonarPost");
        DonarpostRef.keepSynced(true);

        /// blood group button
        aplusbutton = findViewById(R.id.DAbplusButtonID);
        aminusbutton = findViewById(R.id.DAminusButtonID);
        bplusbutton = findViewById(R.id.DBplusButtonID);
        bminusbutton = findViewById(R.id.DBminusButtonID);
        oplusbutton = findViewById(R.id.DOplusButtonID);
        ominusbutton = findViewById(R.id.DOminusButtonID);
        abplusbutton = findViewById(R.id.DAAbplusButtonID);
        abminusbutton = findViewById(R.id.DAbminusButtonID);
        /// blood group button

        ///blood icon image
        aplusicon = findViewById(R.id.DAplusIcon);
        aminusicon = findViewById(R.id.DAminusicon);
        bplusicon = findViewById(R.id.DBplusicon);
        bminusicon = findViewById(R.id.DBminusicon);
        oplusicon = findViewById(R.id.DOplusicon);
        ominusicon = findViewById(R.id.DOminusicon);
        abplusicon = findViewById(R.id.DAbplusicon);
        abminusicon = findViewById(R.id.DAbminusicon);
        ///blood icon image

        aplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "A+";
                aplusicon.setBackgroundResource(R.drawable.cheackbox);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);

                aplusbutton.setBackgroundResource(R.drawable.genderstoke);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        aminusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "A-";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.cheackbox);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);

                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.genderstoke);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        bplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "B+";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.cheackbox);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);

                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.genderstoke);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        bminusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "B-";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.cheackbox);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);


                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.genderstoke);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        oplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "O+";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.cheackbox);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);


                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.genderstoke);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        ominusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "O-";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.cheackbox);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);


                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.genderstoke);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        abplusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "AB+";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.cheackbox);
                abminusicon.setBackgroundResource(R.drawable.clircle_box);

                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.genderstoke);
                abminusbutton.setBackgroundResource(R.drawable.uncheack);
            }
        });

        abminusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bloodtext = "AB-";
                aplusicon.setBackgroundResource(R.drawable.clircle_box);
                aminusicon.setBackgroundResource(R.drawable.clircle_box);
                bplusicon.setBackgroundResource(R.drawable.clircle_box);
                bminusicon.setBackgroundResource(R.drawable.clircle_box);
                oplusicon.setBackgroundResource(R.drawable.clircle_box);
                ominusicon.setBackgroundResource(R.drawable.clircle_box);
                abplusicon.setBackgroundResource(R.drawable.clircle_box);
                abminusicon.setBackgroundResource(R.drawable.cheackbox);


                aplusbutton.setBackgroundResource(R.drawable.uncheack);
                aminusbutton.setBackgroundResource(R.drawable.uncheack);
                bplusbutton.setBackgroundResource(R.drawable.uncheack);
                bminusbutton.setBackgroundResource(R.drawable.uncheack);
                oplusbutton.setBackgroundResource(R.drawable.uncheack);
                ominusbutton.setBackgroundResource(R.drawable.uncheack);
                abplusbutton.setBackgroundResource(R.drawable.uncheack);
                abminusbutton.setBackgroundResource(R.drawable.genderstoke);
            }
        });

        name = findViewById(R.id.DnameID);
        mobilenumber = findViewById(R.id.DMobileNumber);
        location = findViewById(R.id.DLocatonID);
        post = findViewById(R.id.DMessageID);


        femailbutton = findViewById(R.id.DFemailLayoutID);
        malebutton = findViewById(R.id.DMaleLayoutID);
        maleicon = findViewById(R.id.DMale);
        femailicon = findViewById(R.id.DFemaleIcon);

        femailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gendertext = "Female";
                femailbutton.setBackgroundResource(R.drawable.genderstoke);
                malebutton.setBackgroundResource(R.drawable.uncheack);

                femailicon.setBackgroundResource(R.drawable.cheackbox);
                maleicon.setBackgroundResource(R.drawable.clircle_box);
            }
        });
        malebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gendertext = "Male";
                femailicon.setBackgroundResource(R.drawable.clircle_box);
                maleicon.setBackgroundResource(R.drawable.cheackbox);

                femailbutton.setBackgroundResource(R.drawable.uncheack);
                malebutton.setBackgroundResource(R.drawable.genderstoke);
            }
        });


        toolbar = findViewById(R.id.AddPostActivityID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("Donate Post");

        agespinner = findViewById(R.id.SpinnerID);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.age_spinner, R.id.SampleAgeID, ageArray);
        agespinner.setAdapter(adapter);


        ///age spinner
        agespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AgeString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ///age spinner

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddPostActivity.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if(item.getItemId() == R.id.UpdateID){
            updating_addpost();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_buttondesign, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void updating_addpost(){

        String nametext = name.getText().toString();
        //AgeString
        String mobilenumbertext = mobilenumber.getText().toString();
        String locationtext = location.getText().toString();
        String posttext = post.getText().toString();

        if(nametext.isEmpty()){
            name.setError("Name require");
        }
        else if(mobilenumbertext.isEmpty()){
            mobilenumber.setError("Mobile Number require");
        }
        else if(locationtext.isEmpty()){
            location.setError("Location require");
        }
        else if(posttext.isEmpty()){
            post.setError("post require");
        }
        else if (Gendertext == "") {
            Toast.makeText(getApplicationContext(), "please enter your gender", Toast.LENGTH_LONG).show();
        } else if (Bloodtext == "") {
            Toast.makeText(getApplicationContext(), "please enter your blood group", Toast.LENGTH_LONG).show();
        }
        else if(AgeString == ""){
            Toast.makeText(getApplicationContext(), "please enter your age", Toast.LENGTH_LONG).show();
        }
        else {



            Mprogress.setTitle("Please wait ...");
            Mprogress.setMessage("Donor Request is posting wait for a moment");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            Calendar calendartime = Calendar.getInstance();
            SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm:ss");
            CurrentTime = simpleDateFormattime.format(calendartime.getTime());

            Calendar calendardate = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("yyyy-mm-dd");
            CurrentDate = simpleDateFormatdate.format(calendardate.getTime());


            Map donarmap = new HashMap();
            donarmap.put("donar_name", nametext);
            donarmap.put("donar_age", AgeString);
            donarmap.put("donar_gender", Gendertext);
            donarmap.put("donar_number", mobilenumbertext);
            donarmap.put("donar_bloodgroup", Bloodtext);
            donarmap.put("donar_location", locationtext);
            donarmap.put("donar_post", posttext);
            donarmap.put("UID", CurrentUserID);
            donarmap.put("donar_profile_imageURL", Downloaduri);
            donarmap.put("time", CurrentTime);
            donarmap.put("date", CurrentDate);
            donarmap.put("short", countpost);


            DonarpostRef.push().updateChildren(donarmap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Mprogress.dismiss();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "post upload success", Toast.LENGTH_LONG).show();


                                DonarpostRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            countpost = dataSnapshot.getChildrenCount();
                                        }
                                        else {
                                            countpost = 0;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            else {
                                Mprogress.dismiss();
                                Toast.makeText(getApplicationContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileimageuri = result.getUri();
                profileimage.setImageURI(profileimageuri);

                StorageReference filepath = Mprofilestores.child("doner_image").child(profileimageuri.getLastPathSegment());
                filepath.putFile(profileimageuri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    Downloaduri  = task.getResult().getDownloadUrl().toString();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
