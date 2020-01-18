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

public class NeedBloodActivity extends AppCompatActivity {

    private CircleImageView profileimage;
    private EditText name, mobilenumber, hospitalname, hospitallocation;
    private EditText messageinput;
    private long countpost = 0;

    private RelativeLayout malebutton, femailbutton;
    private ImageView maleicon, femailicon;

    private Spinner agespinner;
    private String[] ageArray = {"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44",
            "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66", "67", "68", "69", "70"};
    private String Agetext;
    private Uri profileimageuri;
    private StorageReference Mstores;
    private String Downloadimage;
    private RelativeLayout aplus, aminus, bplus, bminus;
    private RelativeLayout oplus, ominus, abplus, abminus;

    private ImageView aplusicon, aminusicon, bplusicon, bminusicon;
    private ImageView oplusicon, ominusicon, abplusicon, abminusicon;
    private String Bloodtext="";
    private String CurrentDate, CurrentTime;
    private DatabaseReference Mpostdatabase;
    private ProgressDialog Mprogress;
    private FirebaseAuth Mauth;
    private String CurrentUserID;
    private Toolbar toolbar;
    private String gendertext = "";
    private DatabaseReference MuserDatabase;
    private String loginusernameget="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_blood);




        femailbutton = findViewById(R.id.RFemailLayoutID);
        malebutton = findViewById(R.id.RMaleLayoutID);

        maleicon = findViewById(R.id.RMale);
        femailicon = findViewById(R.id.RFemaleIcon);

        femailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gendertext = "Female";
                femailbutton.setBackgroundResource(R.drawable.genderstoke);
                malebutton.setBackgroundResource(R.drawable.uncheack);

                femailicon.setBackgroundResource(R.drawable.cheackbox);
                maleicon.setBackgroundResource(R.drawable.clircle_box);
            }
        });

        malebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gendertext = "Male";
                femailbutton.setBackgroundResource(R.drawable.uncheack);
                malebutton.setBackgroundResource(R.drawable.genderstoke);

                femailicon.setBackgroundResource(R.drawable.clircle_box);
                maleicon.setBackgroundResource(R.drawable.cheackbox);
            }
        });

        toolbar = findViewById(R.id.NeedBloodToolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();

        Mprogress = new ProgressDialog(NeedBloodActivity.this);
        Mpostdatabase = FirebaseDatabase.getInstance().getReference().child("Request_post");
        Mpostdatabase.keepSynced(true);
        messageinput = findViewById(R.id.Rinputmessage);
        aplus = findViewById(R.id.RAplusButtonID);
        aminus = findViewById(R.id.RAminusButtonID);
        bplus = findViewById(R.id.RBplusButtonID);
        bminus = findViewById(R.id.RBminusButtonID);
        oplus = findViewById(R.id.ROplusButtonID);
        ominus = findViewById(R.id.ROminusButtonID);
        abplus = findViewById(R.id.RAbplusButtonID);
        abminus = findViewById(R.id.RAbminusButtonID);


        aplusicon = findViewById(R.id.RAplusIcon);
        aminusicon = findViewById(R.id.RAminusicon);
        bplusicon = findViewById(R.id.RBplusicon);
        bminusicon = findViewById(R.id.RBminusicon);
        oplusicon = findViewById(R.id.ROplusicon);
        ominusicon = findViewById(R.id.ROminusicon);
        abplusicon = findViewById(R.id.RAbplusicon);
        abminusicon = findViewById(R.id.RAbminusicon);

        aplus.setOnClickListener(new View.OnClickListener() {
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


                aplus.setBackgroundResource(R.drawable.genderstoke);
                aminus.setBackgroundResource(R.drawable.uncheack);
                bplus.setBackgroundResource(R.drawable.uncheack);
                bminus.setBackgroundResource(R.drawable.uncheack);
                oplus.setBackgroundResource(R.drawable.uncheack);
                ominus.setBackgroundResource(R.drawable.uncheack);
                abplus.setBackgroundResource(R.drawable.uncheack);
                abminus.setBackgroundResource(R.drawable.uncheack);
            }
        });

        aminus.setOnClickListener(new View.OnClickListener() {
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


                aplus.setBackgroundResource(R.drawable.uncheack);
                aminus.setBackgroundResource(R.drawable.genderstoke);
                bplus.setBackgroundResource(R.drawable.uncheack);
                bminus.setBackgroundResource(R.drawable.uncheack);
                oplus.setBackgroundResource(R.drawable.uncheack);
                ominus.setBackgroundResource(R.drawable.uncheack);
                abplus.setBackgroundResource(R.drawable.uncheack);
                abminus.setBackgroundResource(R.drawable.uncheack);
            }
        });

        bplus.setOnClickListener(new View.OnClickListener() {
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


                aplus.setBackgroundResource(R.drawable.uncheack);
                aminus.setBackgroundResource(R.drawable.uncheack);
                bplus.setBackgroundResource(R.drawable.genderstoke);
                bminus.setBackgroundResource(R.drawable.uncheack);
                oplus.setBackgroundResource(R.drawable.uncheack);
                ominus.setBackgroundResource(R.drawable.uncheack);
                abplus.setBackgroundResource(R.drawable.uncheack);
                abminus.setBackgroundResource(R.drawable.uncheack);
            }
        });

        bminus.setOnClickListener(new View.OnClickListener() {
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


                aplus.setBackgroundResource(R.drawable.uncheack);
                aminus.setBackgroundResource(R.drawable.uncheack);
                bplus.setBackgroundResource(R.drawable.uncheack);
                bminus.setBackgroundResource(R.drawable.genderstoke);
                oplus.setBackgroundResource(R.drawable.uncheack);
                ominus.setBackgroundResource(R.drawable.uncheack);
                abplus.setBackgroundResource(R.drawable.uncheack);
                abminus.setBackgroundResource(R.drawable.uncheack);
            }
        });

        oplus.setOnClickListener(new View.OnClickListener() {
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


                aplus.setBackgroundResource(R.drawable.uncheack);
                aminus.setBackgroundResource(R.drawable.uncheack);
                bplus.setBackgroundResource(R.drawable.uncheack);
                bminus.setBackgroundResource(R.drawable.uncheack);
                oplus.setBackgroundResource(R.drawable.genderstoke);
                ominus.setBackgroundResource(R.drawable.uncheack);
                abplus.setBackgroundResource(R.drawable.uncheack);
                abminus.setBackgroundResource(R.drawable.uncheack);
            }
        });

        ominus.setOnClickListener(new View.OnClickListener() {
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


                aplus.setBackgroundResource(R.drawable.uncheack);
                aminus.setBackgroundResource(R.drawable.uncheack);
                bplus.setBackgroundResource(R.drawable.uncheack);
                bminus.setBackgroundResource(R.drawable.uncheack);
                oplus.setBackgroundResource(R.drawable.uncheack);
                ominus.setBackgroundResource(R.drawable.genderstoke);
                abplus.setBackgroundResource(R.drawable.uncheack);
                abminus.setBackgroundResource(R.drawable.uncheack);
            }
        });

        abplus.setOnClickListener(new View.OnClickListener() {
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


                aplus.setBackgroundResource(R.drawable.uncheack);
                aminus.setBackgroundResource(R.drawable.uncheack);
                bplus.setBackgroundResource(R.drawable.uncheack);
                bminus.setBackgroundResource(R.drawable.uncheack);
                oplus.setBackgroundResource(R.drawable.uncheack);
                ominus.setBackgroundResource(R.drawable.uncheack);
                abplus.setBackgroundResource(R.drawable.genderstoke);
                abminus.setBackgroundResource(R.drawable.uncheack);
            }
        });

        abminus.setOnClickListener(new View.OnClickListener() {
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


                aplus.setBackgroundResource(R.drawable.uncheack);
                aminus.setBackgroundResource(R.drawable.uncheack);
                bplus.setBackgroundResource(R.drawable.uncheack);
                bminus.setBackgroundResource(R.drawable.uncheack);
                oplus.setBackgroundResource(R.drawable.uncheack);
                ominus.setBackgroundResource(R.drawable.uncheack);
                abplus.setBackgroundResource(R.drawable.uncheack);
                abminus.setBackgroundResource(R.drawable.genderstoke);
            }
        });


        Mstores = FirebaseStorage.getInstance().getReference();
        agespinner = findViewById(R.id.RpinnerID);
        profileimage = findViewById(R.id.ReciverPostprofileImageID);
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(NeedBloodActivity.this);
            }
        });

        ArrayAdapter<String> ageadapter = new ArrayAdapter<>(getApplication(), R.layout.age_spinner, R.id.SampleAgeID, ageArray);
        agespinner.setAdapter(ageadapter);

        agespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Agetext = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        profileimage = findViewById(R.id.ReciverPostprofileImageID);
        name = findViewById(R.id.RnameID);
        mobilenumber = findViewById(R.id.RMobileNumber);
        hospitalname = findViewById(R.id.RHospitalInput);
        hospitallocation = findViewById(R.id.RLocationInput);


        Mpostdatabase.addValueEventListener(new ValueEventListener() {
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

        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabase.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                     loginusernameget = dataSnapshot.child("Name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileimageuri = result.getUri();
                profileimage.setImageURI(profileimageuri);

                StorageReference filepath = Mstores.child("reciverprofile_image").child(profileimageuri.getLastPathSegment());
                filepath.putFile(profileimageuri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    Downloadimage = task.getResult().getDownloadUrl().toString();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_buttondesign, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.UpdateID){
            StartingUpdatepost();
        }
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void StartingUpdatepost(){






        String nametext = name.getText().toString();
        String mobilenumbertext = mobilenumber.getText().toString();
        String hospitalnametext = hospitalname.getText().toString();
        String locationtext = hospitallocation.getText().toString();
        String messagetext = messageinput.getText().toString();


        if(nametext.isEmpty()){
            name.setError("Name require");
        }
        else if(messagetext.isEmpty()){
            messageinput.setError("Your post require");
        }
        else if(gendertext==""){
            Toast.makeText(getApplicationContext(), "gender is empty", Toast.LENGTH_LONG).show();
        }
        else if(mobilenumbertext.isEmpty()){
            mobilenumber.setError("Number require");
        }
        else if(hospitalnametext.isEmpty()){
            hospitalname.setError("Hospital Name require");
        }
        else if(locationtext.isEmpty()){
            hospitallocation.setError("Location require");
        }
        else if(Bloodtext==""){
            Toast.makeText(getApplicationContext(), "input your blood group", Toast.LENGTH_LONG).show();
        }
        else {

            Mprogress.setTitle("Please wait");
            Mprogress.setMessage("Uploading your post");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            Calendar calendartime = Calendar.getInstance();
            SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("hh:mm:ss");
            CurrentTime = simpleDateFormattime.format(calendartime.getTime());

            Calendar calendardate = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("dd-M-yyyy");
            CurrentDate = simpleDateFormatdate.format(calendardate.getTime());


            Map postmap = new HashMap();
            postmap.put("patentname", nametext);
            postmap.put("mobilenumber", mobilenumbertext);
            postmap.put("hospital_name", hospitalnametext);
            postmap.put("location", locationtext);
            postmap.put("message", messagetext);
            postmap.put("userid", CurrentUserID);
            postmap.put("Image_downloadurl", Downloadimage);
            postmap.put("blood_group", Bloodtext);
            postmap.put("gender", gendertext);
            postmap.put("age", Agetext);
            postmap.put("counter", countpost);
            postmap.put("loginusername", loginusernameget);



            Mpostdatabase.push().updateChildren(postmap)
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
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
