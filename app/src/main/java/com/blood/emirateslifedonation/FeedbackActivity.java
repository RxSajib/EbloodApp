package com.blood.emirateslifedonation;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.blood.emirateslifedonation.GmailApi.Post;
import com.blood.emirateslifedonation.GmailApi.Postt;
import com.hsalf.smilerating.SmileRating;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackActivity extends AppCompatActivity {

    private EditText phonenumber, comments;
    private Button submitbutton;
    private SmileRating rating;
    private String RatingText = "";
    private ProgressDialog Mprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        phonenumber = findViewById(R.id.MobileNumberID);
        comments = findViewById(R.id.CommentsID);
        submitbutton = findViewById(R.id.SubmitButtonID);
        rating = findViewById(R.id.smile_rating);


        rating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {

                switch (smiley){
                    case SmileRating.TERRIBLE:
                        RatingText = "Terrible";
                        break;

                    case SmileRating.BAD:
                        RatingText = "Bad";
                        break;

                    case SmileRating.OKAY:
                        RatingText = "Okay";
                        break;

                    case SmileRating.GOOD:
                        RatingText = "Good";
                        break;

                    case SmileRating.GREAT:
                        RatingText = "Great";
                        break;
                }
            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startposting();
            }
        });

    }

    private void startposting(){

        final String phone = phonenumber.getText().toString().trim();
        String message = comments.getText().toString();

        if(phone.isEmpty()){
            phonenumber.setError("Number require");
        }
        else if(message.isEmpty()){
            comments.setError("Comments require");
        }
        else {

            Mprogress.setMessage("Sending ...");
            Mprogress.setTitle("Please wait");
            Mprogress.setCanceledOnTouchOutside(false);
            Mprogress.show();

            String MESSAGE  = "Phone Number :"+phone+"\n"+"Message :"+message+"\n"+"Rating :"+RatingText+"\n";


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://hsmailapi.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            /////send email

            Post sendMailAPIClint = retrofit.create(Post.class);


            Postt postt = new Postt("tomastechinfo@gmail.com", "Mangrovehotel2019", "sajibroy206@gmail.com", "FeedBack From SBB", MESSAGE);

            Call<Post> call = sendMailAPIClint.createPost(postt);

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {

                    if(!response.isSuccessful()){
                        Mprogress.dismiss();
                        phonenumber.setText("");
                        comments.setText("");
                    }
                    else {
                        Mprogress.dismiss();
                        phonenumber.setText("");
                        comments.setText("");
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Mprogress.dismiss();
                    phonenumber.setText("");
                    comments.setText("");
                }
            });

        }
    }
}
