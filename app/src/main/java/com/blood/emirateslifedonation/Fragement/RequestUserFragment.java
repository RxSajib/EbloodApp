package com.blood.emirateslifedonation.Fragement;


import android.app.AlertDialog;
import android.app.UiAutomation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.blood.emirateslifedonation.ChatActivity;
import com.blood.emirateslifedonation.HolderClass.reciver_holder;
import com.blood.emirateslifedonation.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestUserFragment extends Fragment {

    private RecyclerView requestview;
    private DatabaseReference MRequestdataase;
    private String CurrentUserID;
    private FirebaseAuth Mauth;
    private DatabaseReference LikeRef;
    boolean cheacker = false;


    private RelativeLayout requesterlayout;
    private InterstitialAd interstitialAd;
    private SearchView searchView;

    public RequestUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MRequestdataase = FirebaseDatabase.getInstance().getReference().child("Request_post");
        MRequestdataase.keepSynced(true);

        View view = inflater.inflate(R.layout.fragment_request_user, container, false);

        searchView = view.findViewById(R.id.RequesterSearchID);


        requesterlayout = view.findViewById(R.id.RequestLayoutID);
        requesterlayout.setVisibility(View.VISIBLE);
        LikeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        LikeRef.keepSynced(true);

        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        requestview = view.findViewById(R.id.UserRecylearViewID);
        requestview.setHasFixedSize(true);
        requestview.setLayoutManager(new LinearLayoutManager(getContext()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                startsearching(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                startsearching(newText);
                return false;
            }
        });


        return view;
    }

    private void startsearching(final String text){

        String quary = text.toLowerCase();
        Query firebaseqry = MRequestdataase.orderByChild("search").startAt(quary).endAt(quary+"\uf8ff");


        FirebaseRecyclerAdapter<reciver_holder, RequestHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<reciver_holder, RequestHolder>(
                reciver_holder.class,
                R.layout.request_banner,
                RequestHolder.class,
                firebaseqry
        ) {
            @Override
            protected void populateViewHolder(final RequestHolder requestHolder, final reciver_holder reciver_holder, int i) {

                final String UID = getRef(i).getKey();
                MRequestdataase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        requestHolder.setLikeButtonStatas(UID);


                        if(dataSnapshot.exists()){

                            requesterlayout.setVisibility(View.GONE);
                            if(dataSnapshot.hasChild("userid")){
                                String uid = dataSnapshot.child("userid").getValue().toString();

                                if(uid.equals(CurrentUserID)){
                                    requestHolder.chatimage.setVisibility(View.GONE);
                                }
                            }

                            if(dataSnapshot.hasChild("Image_downloadurl")){
                                String Image_downloadurlget = dataSnapshot.child("Image_downloadurl").getValue().toString();
                                requestHolder.setprofileimageset(Image_downloadurlget);
                            }
                            if(dataSnapshot.hasChild("blood_group")){
                                String blood_groupget = dataSnapshot.child("blood_group").getValue().toString();
                                requestHolder.setBloodgroupset(blood_groupget);
                            }
                            if(dataSnapshot.hasChild("location")){
                                String locationget = dataSnapshot.child("location").getValue().toString();
                                requestHolder.setLocationset(locationget);
                            }
                            if(dataSnapshot.hasChild("message")){
                                String messageget = dataSnapshot.child("message").getValue().toString();
                                requestHolder.setmessage(messageget);
                            }
                            if(dataSnapshot.hasChild("mobilenumber")){
                                String mobilenumberget = dataSnapshot.child("mobilenumber").getValue().toString();
                                requestHolder.setmobileset(mobilenumberget);
                            }
                            if(dataSnapshot.hasChild("patentname")){
                                String patentnameget = dataSnapshot.child("patentname").getValue().toString();
                                requestHolder.setUsernameset(patentnameget);
                            }
                            if(dataSnapshot.hasChild("gender")){
                                String genderget = dataSnapshot.child("gender").getValue().toString();
                                requestHolder.setGenderset(genderget);
                            }
                            if(dataSnapshot.hasChild("age")){
                                String ageget = dataSnapshot.child("age").getValue().toString();
                                requestHolder.setAgeset(ageget);
                            }

                            if(dataSnapshot.hasChild("loginusername")){
                                String loginusernameget =dataSnapshot.child("loginusername").getValue().toString();
                                requestHolder.setcurrentnameset(loginusernameget);
                            }

                            /// like add
                            requestHolder.likeimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    interstitialAd = new InterstitialAd(getContext());
                                    interstitialAd.setAdUnitId("ca-app-pub-3947412102662378/8621058376");
                                    interstitialAd.loadAd(new AdRequest.Builder().build());

                                    interstitialAd.setAdListener(new AdListener(){
                                        @Override
                                        public void onAdClosed() {
                                            super.onAdClosed();
                                        }
                                    });
                                }
                            });
                            /// like add

                            ///liek function
                            requestHolder.likeimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cheacker = true;
                                    LikeRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(cheacker == true) {

                                                if (dataSnapshot.child(UID).hasChild(CurrentUserID)) {
                                                    LikeRef.child(UID).child(CurrentUserID).removeValue();
                                                    cheacker = false;
                                                } else {
                                                    LikeRef.child(UID).child(CurrentUserID).setValue(true);
                                                    cheacker = false;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            ///liek function


                            requestHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String uid = dataSnapshot.child("userid").getValue().toString();

                                    if(uid.equals(CurrentUserID)){
                                        requestHolder.chatimage.setVisibility(View.INVISIBLE);
                                    }
                                    else {

                                        requestHolder.chatimage.setVisibility(View.VISIBLE);


                                        android.app.AlertDialog.Builder Mbuilder = new android.app.AlertDialog.Builder(getContext());
                                        Mbuilder.setTitle("Select Options");
                                        CharSequence charSequence[] = new CharSequence[]{
                                                "Open Chat"
                                        };

                                        Mbuilder.setItems(charSequence, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(which == 0){
                                                    String ChatUid = dataSnapshot.child("userid").getValue().toString();
                                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                                    intent.putExtra("KEY", ChatUid);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                }

                                            }
                                        });

                                        AlertDialog alertDialog = Mbuilder.create();
                                        alertDialog.show();
                                    }
                                }
                            });
                        }
                        else {
                            requesterlayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        requestview.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public void onStart() {


        FirebaseRecyclerAdapter<reciver_holder, RequestHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<reciver_holder, RequestHolder>(
                reciver_holder.class,
                R.layout.request_banner,
                RequestHolder.class,
                MRequestdataase.orderByChild("counter")
        ) {
            @Override
            protected void populateViewHolder(final RequestHolder requestHolder, final reciver_holder reciver_holder, int i) {

                final String UID = getRef(i).getKey();
                MRequestdataase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        requestHolder.setLikeButtonStatas(UID);


                        if(dataSnapshot.exists()){

                            requesterlayout.setVisibility(View.GONE);
                            if(dataSnapshot.hasChild("userid")){
                                String uid = dataSnapshot.child("userid").getValue().toString();

                                if(uid.equals(CurrentUserID)){
                                    requestHolder.chatimage.setVisibility(View.GONE);
                                }
                            }

                            if(dataSnapshot.hasChild("Image_downloadurl")){
                                String Image_downloadurlget = dataSnapshot.child("Image_downloadurl").getValue().toString();
                                requestHolder.setprofileimageset(Image_downloadurlget);
                            }
                            if(dataSnapshot.hasChild("blood_group")){
                                String blood_groupget = dataSnapshot.child("blood_group").getValue().toString();
                                requestHolder.setBloodgroupset(blood_groupget);
                            }
                            if(dataSnapshot.hasChild("location")){
                                String locationget = dataSnapshot.child("location").getValue().toString();
                                requestHolder.setLocationset(locationget);
                            }
                            if(dataSnapshot.hasChild("message")){
                                String messageget = dataSnapshot.child("message").getValue().toString();
                                requestHolder.setmessage(messageget);
                            }
                            if(dataSnapshot.hasChild("mobilenumber")){
                                String mobilenumberget = dataSnapshot.child("mobilenumber").getValue().toString();
                                requestHolder.setmobileset(mobilenumberget);
                            }
                            if(dataSnapshot.hasChild("patentname")){
                                String patentnameget = dataSnapshot.child("patentname").getValue().toString();
                                requestHolder.setUsernameset(patentnameget);
                            }
                            if(dataSnapshot.hasChild("gender")){
                                String genderget = dataSnapshot.child("gender").getValue().toString();
                                requestHolder.setGenderset(genderget);
                            }
                            if(dataSnapshot.hasChild("age")){
                                String ageget = dataSnapshot.child("age").getValue().toString();
                                requestHolder.setAgeset(ageget);
                            }

                            if(dataSnapshot.hasChild("loginusername")){
                                String loginusernameget =dataSnapshot.child("loginusername").getValue().toString();
                                requestHolder.setcurrentnameset(loginusernameget);
                            }

                            /// like add
                            requestHolder.likeimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    interstitialAd = new InterstitialAd(getContext());
                                    interstitialAd.setAdUnitId("ca-app-pub-3947412102662378/8621058376");
                                    interstitialAd.loadAd(new AdRequest.Builder().build());

                                    interstitialAd.setAdListener(new AdListener(){
                                        @Override
                                        public void onAdClosed() {
                                            super.onAdClosed();
                                        }
                                    });
                                }
                            });
                            /// like add

                            ///liek function
                            requestHolder.likeimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cheacker = true;
                                    LikeRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(cheacker == true) {

                                                if (dataSnapshot.child(UID).hasChild(CurrentUserID)) {
                                                    LikeRef.child(UID).child(CurrentUserID).removeValue();
                                                    cheacker = false;
                                                } else {
                                                    LikeRef.child(UID).child(CurrentUserID).setValue(true);
                                                    cheacker = false;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            ///liek function


                            requestHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String uid = dataSnapshot.child("userid").getValue().toString();

                                    if(uid.equals(CurrentUserID)){
                                        requestHolder.chatimage.setVisibility(View.INVISIBLE);
                                    }
                                    else {

                                        requestHolder.chatimage.setVisibility(View.VISIBLE);


                                        android.app.AlertDialog.Builder Mbuilder = new android.app.AlertDialog.Builder(getContext());
                                        Mbuilder.setTitle("Select Options");
                                        CharSequence charSequence[] = new CharSequence[]{
                                                "Open Chat"
                                        };

                                        Mbuilder.setItems(charSequence, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(which == 0){
                                                    String ChatUid = dataSnapshot.child("userid").getValue().toString();
                                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                                    intent.putExtra("KEY", ChatUid);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                }

                                            }
                                        });

                                        AlertDialog alertDialog = Mbuilder.create();
                                        alertDialog.show();
                                    }
                                }
                            });
                        }
                        else {
                            requesterlayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        requestview.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class RequestHolder extends RecyclerView.ViewHolder{

        private View Mview;
        private CircleImageView profileimage;
        private TextView username;
        private TextView bloodgroup, age, gender, mobile, location;
        private TextView posttext;
        private Context context;
        private ImageView chatimage;
        private ImageView likeimage;
        private TextView likecounter;

        private DatabaseReference Mlikedatabase;
        private int Likecounter;
        private String CurrentuseID;
        private TextView currentusername;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            currentusername = Mview.findViewById(R.id.CurrentUserNameID);
            context = Mview.getContext();
            profileimage = Mview.findViewById(R.id.ReciverProfileImageID);
            username = Mview.findViewById(R.id.RequestusernameID);
            bloodgroup = Mview.findViewById(R.id.RequestBloodGroupID);
            age = Mview.findViewById(R.id.ReciverAgeID);
            gender = Mview.findViewById(R.id.ReciverGenderID);
            mobile = Mview.findViewById(R.id.ReciverMobile);
            location = Mview.findViewById(R.id.ReciverLocation);
            posttext = Mview.findViewById(R.id.ReciverPostID);
            likeimage = Mview.findViewById(R.id.ReciverLoveicon);
            likecounter = Mview.findViewById(R.id.ReciverLovecounterID);

            chatimage = Mview.findViewById(R.id.ChatImageID);

            Mlikedatabase = FirebaseDatabase.getInstance().getReference().child("Likes");
            CurrentuseID = FirebaseAuth.getInstance().getCurrentUser().getUid();



        }

        private void setLikeButtonStatas(final String UID){
            Mlikedatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(UID).hasChild(CurrentuseID)){
                        Likecounter = (int) dataSnapshot.child(UID).getChildrenCount();
                        likeimage.setImageResource(R.drawable.redlove);
                        likecounter.setText(Integer.toString(Likecounter)+" Like");
                    }
                    else {
                        Likecounter = (int) dataSnapshot.child(UID).getChildrenCount();
                        likeimage.setImageResource(R.drawable.blacklove);
                        likecounter.setText(Integer.toString(Likecounter)+" Like");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void setprofileimageset(String img){
            Picasso.with(context).load(img).placeholder(R.drawable.defaultimage).into(profileimage);

            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(profileimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }

        public void setcurrentnameset(String nam){
            currentusername.setText(nam);
        }

        public void setUsernameset(String nam){
            username.setText(nam);
        }

        public void setBloodgroupset(String blood){
            bloodgroup.setText(blood);
        }

        public void setAgeset(String aa){
            age.setText(aa);
        }

        public void setGenderset(String gen){
            gender.setText(gen);
        }

        public void setmobileset(String mob){
            mobile.setText(mob);
        }

        public void setLocationset(String loc){
            location.setText(loc);
        }

        public void setmessage(String mess){
            posttext.setText(mess);
        }
    }
}
