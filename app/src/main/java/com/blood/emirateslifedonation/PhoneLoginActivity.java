package com.blood.emirateslifedonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private EditText phonenumber;
    private Button loginbutton;
    private String codeSent;
    private EditText codetext;
    private FirebaseAuth Mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        Mauth = FirebaseAuth.getInstance();
        codetext = findViewById(R.id.PhoneNumberCodeID);
        loginbutton = findViewById(R.id.PhoneLoginButtonID);
        phonenumber = findViewById(R.id.PhoneNumberID);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = codetext.getText().toString();
                String number = phonenumber.getText().toString();

                if(code.isEmpty()){
                    codetext.setError("Code require");
                }
                else if(number.isEmpty()){
                    phonenumber.setError("Number require");
                }
                else {
                    String phonenumber  = code+number;
                    Intent intent = new Intent(getApplicationContext(), PhoneOTPLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("number", phonenumber);
                    startActivity(intent);


                }
            }
        });
    }


    @Override
    protected void onStop() {
        FirebaseUser Muser = Mauth.getCurrentUser();
        if(Muser != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        super.onStop();
    }
}
