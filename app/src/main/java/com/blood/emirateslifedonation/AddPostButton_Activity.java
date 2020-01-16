package com.blood.emirateslifedonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AddPostButton_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cardViewdonate, cardViewneedblood;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_button_);


        toolbar = findViewById(R.id.AddPistToolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("Add Your Post");


        cardViewdonate = findViewById(R.id.CCone);
        cardViewneedblood = findViewById(R.id.CCtwo);


        cardViewdonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitialAd = new InterstitialAd(getApplicationContext());
                interstitialAd.setAdUnitId("ca-app-pub-3947412102662378/8621058376");
                interstitialAd.loadAd(new AdRequest.Builder().build());

                interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }
                });
                Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        cardViewneedblood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitialAd = new InterstitialAd(getApplicationContext());
                interstitialAd.setAdUnitId("ca-app-pub-3947412102662378/8621058376");
                interstitialAd.loadAd(new AdRequest.Builder().build());

                interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }
                });
                Intent intent = new Intent(getApplicationContext(), NeedBloodActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
