package com.blood.emirateslifedonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blood.emirateslifedonation.Adapter.HomeTabMaker;
import com.blood.emirateslifedonation.Model.Token;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomeTabMaker homeTabMaker;
    private FirebaseAuth Mauth;
    private DatabaseReference Muserdatabse;
    private String CurrentUser;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        adView = findViewById(R.id.DonarBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        fcm_token();

        Mauth = FirebaseAuth.getInstance();
        CurrentUser = Mauth.getCurrentUser().getUid();
        toolbar = findViewById(R.id.HomeToolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);

        drawerLayout = findViewById(R.id.DrawerID);
        navigationView = findViewById(R.id.NavagationID);

        tabLayout = findViewById(R.id.TabLayoutID);
        viewPager = findViewById(R.id.ViewPaferID);

        homeTabMaker = new HomeTabMaker(getSupportFragmentManager());
        viewPager.setAdapter(homeTabMaker);
        tabLayout.setupWithViewPager(viewPager);
        Muserdatabse = FirebaseDatabase.getInstance().getReference().child("Users");
        Muserdatabse.keepSynced(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.ProfileID){
                    menuItem.setChecked(true);
                    menuItem.setCheckable(true);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(menuItem.getItemId() == R.id.LogOutID){
                    Mauth.signOut();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                if(menuItem.getItemId() == R.id.ShareID){
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawer(Gravity.LEFT);


                    int counter = 0;
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    String shareMessage = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";

                    String sharebody = shareMessage;
                    String sharesubject = "“A bottle of blood saved my life, was it yours.” “Every blood donor is a life saver.”\n" +
                            "download the link below share with your all friends To Get REAL blessing " + "\n\n"+sharebody;
                    intent.putExtra(Intent.EXTRA_TEXT, sharesubject);
                    //  intent.putExtra(Intent.EXTRA_SUBJECT, sharebody);
                    startActivity(Intent.createChooser(intent, "share with"));
                }

                if(menuItem.getItemId() == R.id.StokeID){
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getApplicationContext(), StokeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.FeedbackID){
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                if(menuItem.getItemId() == R.id.SupportUsID){
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getApplicationContext(), OneTouchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                return true;
            }
        });

        View view = navigationView.inflateHeaderView(R.layout.header_layout);
        final TextView username = view.findViewById(R.id.Username);
        final TextView userblood = view.findViewById(R.id.UserBlood);
        final CircleImageView image = view.findViewById(R.id.ImageViewID);


        Muserdatabse.child(CurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("Name")){
                        String Nameget = dataSnapshot.child("Name").getValue().toString();
                        username.setText(Nameget);
                    }
                    if(dataSnapshot.hasChild("blood")){
                        String bloodget = dataSnapshot.child("blood").getValue().toString();
                        userblood.setText(bloodget);
                    }
                    if(dataSnapshot.hasChild("imageurl")){
                        String imageurlget = dataSnapshot.child("imageurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageurlget).placeholder(R.drawable.defaultimage).into(image);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "no user found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(Gravity.LEFT);
        }

        if(item.getItemId() == R.id.AddPostID){
            Intent intent = new Intent(getApplicationContext(), AddPostButton_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void fcm_token() {



        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        //  if (refreshedToken!="")



        Log.i("TAG", "fcm_token: "+refreshedToken);


        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference referance=db.getReference("Token");
        Token token=new Token(refreshedToken,true);
        referance.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);


    }

    @Override
    protected void onStart() {

        final FirebaseUser user = Mauth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {


            Muserdatabse.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(!dataSnapshot.hasChild(CurrentUser)){
                        Intent intent = new Intent(getApplicationContext(), ProfileActivty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        super.onStart();
    }
}
