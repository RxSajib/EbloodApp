package com.blood.emirateslifedonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegiaterActivity extends AppCompatActivity {

    private EditText email, password, retypepassword;
    private Button regbutton;
    private FirebaseAuth Mauth;
    private ProgressDialog Mprogress;
    private TextView haveaccount;

    private RelativeLayout googlebutton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    private TextView phonelogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiater);

        phonelogin = findViewById(R.id.PhoneLoginID);
        phonelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        googlebutton = findViewById(R.id.RegistergoogleButtonID);
        Mauth = FirebaseAuth.getInstance();
        haveaccount = findViewById(R.id.HaveAccountID);
        Mprogress = new ProgressDialog(RegiaterActivity.this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        email = findViewById(R.id.REMailAddressID);
        password = findViewById(R.id.RRPasswordID);
        retypepassword = findViewById(R.id.RCPasswordID);

        regbutton = findViewById(R.id.RegisterButtonID);

        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email_colour, 0, 0, 0);

                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email_colour, 0, 0, 0);

                }

                return false;
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_colour, 0, 0, 0);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_colour, 0, 0, 0);

                }

                return false;
            }
        });

        retypepassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    retypepassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_colour, 0, 0, 0);

                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    retypepassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_colour, 0, 0, 0);

                }
                return false;
            }
        });

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailtext = email.getText().toString();
                String passwordtext = password.getText().toString();
                String rpasswordtext = retypepassword.getText().toString();

                if (emailtext.isEmpty()) {
                    email.setError("Email require");
                } else if (passwordtext.isEmpty()) {
                    password.setError("Password require");
                } else if (rpasswordtext.isEmpty()) {
                    retypepassword.setError("Password require");
                } else if (!passwordtext.equals(rpasswordtext)) {
                    email.setText(null);
                    password.setError(null);
                    retypepassword.setError(null);
                    password.setError("Password error");
                    retypepassword.setError("Password error");
                } else if (passwordtext.length() <= 7) {
                    Toast.makeText(getApplicationContext(), "Password AtList 8 char need", Toast.LENGTH_LONG).show();
                } else {
                    Mprogress.setTitle("Please wait ...");
                    Mprogress.setMessage("We are creating new account");
                    Mprogress.setCanceledOnTouchOutside(false);
                    Mprogress.show();
                    Mauth.createUserWithEmailAndPassword(emailtext, passwordtext)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Mprogress.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Mprogress.dismiss();
                                        email.setText(null);
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
        });

        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        googlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        FirebaseUser Muser = Mauth.getCurrentUser();
        if (Muser != null) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            sendBroadcast(intent);
            finish();
        }
        super.onStart();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            FirebaseUser user = Mauth.getCurrentUser();
                            //   updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //   Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }

                        // ...
                    }
                });


    }

}
