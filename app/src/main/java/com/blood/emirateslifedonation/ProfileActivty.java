package com.blood.emirateslifedonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivty extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private RelativeLayout male, femail;
    private ImageView maleicon, femailicon;
    private EditText fullname, mobilenumber, location, email;
    private CircleImageView profileimage;


    ///blood button
    private RelativeLayout aplus, aminus, bplus, bminus;
    private RelativeLayout oplus, ominus, abplus, abminus;

    ///button icon
    private ImageView aplusicon, aminusicon, bplusicon, bminusicon;
    private ImageView oplusicon, ominusicon, abplusicon, abminusicon;
    private Uri imageuri = null;
    private StorageReference Mprofileimagestor;

    ///datepcker
    private TextView selectedete;
    private RelativeLayout pickupdate;
    private String date = "", gender = "", bloodgroup = "";
    ///datepcker

    private Button setupbutton;
    private DatabaseReference MuserDatabase;
    private FirebaseAuth Mauth;
    private String CurrentUserID;
    private String DownloadImage;

    private ProgressDialog Mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activty);


        profileimage = findViewById(R.id.ProfileImageIDID);
        Mprofileimagestor = FirebaseStorage.getInstance().getReference();
        setupbutton = findViewById(R.id.RegisterButtID);
        email = findViewById(R.id.EmailInputID);
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabase.keepSynced(true);

        Mprogress = new ProgressDialog(ProfileActivty.this);
        ///date
        pickupdate = findViewById(R.id.DateButtonID);
        selectedete = findViewById(R.id.BithDateInput);

        pickupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdatepicker_diolog();
            }
        });
        ///date

        /// blood button
        aplus = findViewById(R.id.AbplusButtonID);
        aminus = findViewById(R.id.AminusButtonID);
        bplus = findViewById(R.id.BplusButtonID);
        bminus = findViewById(R.id.BminusButtonID);
        oplus = findViewById(R.id.OplusButtonID);
        ominus = findViewById(R.id.OminusButtonID);
        abplus = findViewById(R.id.AAbplusButtonID);
        abminus = findViewById(R.id.AbminusButtonID);
        /// blood button

        ///blood image
        aplusicon = findViewById(R.id.AplusIcon);
        aminusicon = findViewById(R.id.Aminusicon);
        bplusicon = findViewById(R.id.Bplusicon);
        bminusicon = findViewById(R.id.Bminusicon);
        oplusicon = findViewById(R.id.Oplusicon);
        ominusicon = findViewById(R.id.Ominusicon);
        abplusicon = findViewById(R.id.Abplusicon);
        abminusicon = findViewById(R.id.Abminusicon);
        ///blood image


        aplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bloodgroup = "A+";
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
                bloodgroup = "A-";
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
                bloodgroup = "B+";
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
                bloodgroup = "B-";
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
                bloodgroup = "O+";
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
                bloodgroup = "O-";
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
                bloodgroup = "AB+";
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
                bloodgroup = "AB-";
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

        maleicon = findViewById(R.id.Male);
        femailicon = findViewById(R.id.FemaleIcon);
        fullname = findViewById(R.id.FullnameInputTextID);
        mobilenumber = findViewById(R.id.MobileNumberInputID);

        male = findViewById(R.id.MaleLayoutID);
        femail = findViewById(R.id.FemailLayoutID);
        location = findViewById(R.id.LocationInputID);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Male";
                maleicon.setBackgroundResource(R.drawable.cheackbox);
                femailicon.setBackgroundResource(R.drawable.clircle_box);
                male.setBackgroundResource(R.drawable.genderstoke);
                femail.setBackgroundResource(R.drawable.uncheack);
            }
        });

        femail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Female";
                femailicon.setBackgroundResource(R.drawable.cheackbox);
                maleicon.setBackgroundResource(R.drawable.clircle_box);
                femail.setBackgroundResource(R.drawable.genderstoke);
                male.setBackgroundResource(R.drawable.uncheack);
            }
        });


        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileActivty.this);

            }
        });


        setupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startuploading_date();
            }
        });
    }


    ///image geting
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();
                profileimage.setImageURI(imageuri);

                StorageReference filepath = Mprofileimagestor.child("profile_image").child(imageuri.getLastPathSegment());
                filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            DownloadImage = task.getResult().getDownloadUrl().toString();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }
    }
    ///image geting

    private void showdatepicker_diolog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "/" + month + 1 + "/" + year;
        selectedete.setText(date);
    }

    private void startuploading_date() {
        String emailtext = email.getText().toString();
        String fullnametext = fullname.getText().toString();
        String mobilenumbertext = mobilenumber.getText().toString();
        String locationtext = location.getText().toString();


        if (fullnametext.isEmpty()) {
            fullname.setError("Name require");
        } else if (mobilenumbertext.isEmpty()) {
            mobilenumber.setError("Number require");
        } else if (locationtext.isEmpty()) {
            location.setError("Location require");
        } else if (gender == "") {
            Toast.makeText(getApplicationContext(), "please select your gender", Toast.LENGTH_LONG).show();
        } else if (date == "") {
            Toast.makeText(getApplicationContext(), "please select your date of birth", Toast.LENGTH_LONG).show();
        } else if (bloodgroup == "") {
            Toast.makeText(getApplicationContext(), "Please select your blood group", Toast.LENGTH_LONG).show();
        }
        /*else if(DownloadImage==""){
            Toast.makeText(getApplicationContext(), "setup your profilr image", Toast.LENGTH_LONG).show();
        }*/
        else {

            Mprogress.setTitle("Please wait ...");
            Mprogress.setMessage("saving your profile information");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            Map usermap = new HashMap();
            usermap.put("Name", fullnametext);
            usermap.put("email", emailtext);
            usermap.put("phone", mobilenumbertext);
            usermap.put("location", locationtext);
            usermap.put("imageurl", DownloadImage);
            usermap.put("blood", bloodgroup);
            usermap.put("sex", gender);
            usermap.put("devices_token", FirebaseInstanceId.getInstance().getToken().toString());
           // usermap.put("search", nametext.toLowerCase());

            MuserDatabase.child(CurrentUserID).updateChildren(usermap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Mprogress.dismiss();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
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


}
