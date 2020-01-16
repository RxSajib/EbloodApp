package com.blood.emirateslifedonation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class OneTouchActivity extends AppCompatActivity {

    private CardView cardViewone, cardViewtwo, cardViewthree;
    private AdView mAdview, adtwo;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_touch);

        mAdview = findViewById(R.id.BannerAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        adtwo = findViewById(R.id.AdtwoID);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adtwo.loadAd(adRequest1);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3947412102662378/8621058376");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });


        cardViewone = findViewById(R.id.AdmobOneID);
        cardViewtwo = findViewById(R.id.AdmobTwo);
        cardViewthree = findViewById(R.id.AdmobThree);

        cardViewone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {

                }
            }
        });

        cardViewtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {

                }
            }
        });

        cardViewthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                }
                else {

                }
            }
        });
    }
}
