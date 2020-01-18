package com.blood.emirateslifedonation.Fragement;


import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.blood.emirateslifedonation.ChatActivity;
import com.blood.emirateslifedonation.HolderClass.donar_holder;
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
public class DonerFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference Mdonarpostdatabase;
    private String CurrentUserID;
    private FirebaseAuth Mauth;
    private Boolean LikeState = false;
    private DatabaseReference MLikeRef;
    private RelativeLayout donarlayout;
    private Query firebaseqry;
    private InterstitialAd interstitialAd;
    private AdView adView;



    public DonerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Mdonarpostdatabase = FirebaseDatabase.getInstance().getReference().child("DonarPost");
        Mdonarpostdatabase.keepSynced(true);

         firebaseqry = Mdonarpostdatabase.orderByChild("short");

        MLikeRef = FirebaseDatabase.getInstance().getReference().child("DonarLike");
        MLikeRef.keepSynced(true);
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        View view = inflater.inflate(R.layout.fragment_doner, container, false);




        donarlayout = view.findViewById(R.id.DonorLayoutID);
        donarlayout.setVisibility(View.VISIBLE);
        recyclerView = view.findViewById(R.id.DonarViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        displayalluser();

        return view;
    }


    private void displayalluser(){


        FirebaseRecyclerAdapter<donar_holder, DonarViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<donar_holder, DonarViewHolder>(
                donar_holder.class,
                R.layout.samplebanner,
                DonarViewHolder.class,
                firebaseqry
        ) {
            @Override
            protected void populateViewHolder(final DonarViewHolder donarViewHolder, final donar_holder donar_holder, int i) {

                final String UID = getRef(i).getKey();
                Mdonarpostdatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {

                                donarViewHolder.setReadingLike(UID);

                                if(dataSnapshot.exists()){
                                    donarlayout.setVisibility(View.GONE);
                                    if(dataSnapshot.hasChild("UID")){
                                        String UIDget = dataSnapshot.child("UID").getValue().toString();
                                        if(UIDget.equals(CurrentUserID)){
                                            donarViewHolder.chatimage.setVisibility(View.GONE);
                                        }

                                    }

                                    if(dataSnapshot.hasChild("date")){
                                        String dateget = dataSnapshot.child("date").getValue().toString();
                                        donarViewHolder.setdateset(dateget);
                                    }

                                    if(dataSnapshot.hasChild("donar_bloodgroup")){
                                        String donar_bloodgroupget = dataSnapshot.child("donar_bloodgroup").getValue().toString();
                                        donarViewHolder.setbloodgoupset(donar_bloodgroupget);
                                    }

                                    if(dataSnapshot.hasChild("donar_number")){
                                        String donar_numberget = dataSnapshot.child("donar_number").getValue().toString();
                                        donarViewHolder.setPhonenumberset(donar_numberget);
                                    }

                                    if(dataSnapshot.hasChild("donar_profile_imageURL")){
                                        String donar_profile_imageURLget = dataSnapshot.child("donar_profile_imageURL").getValue().toString();
                                        donarViewHolder.setprofileimageset(donar_profile_imageURLget);
                                    }

                                    if(dataSnapshot.hasChild("donar_post")){
                                        String donar_postget = dataSnapshot.child("donar_post").getValue().toString();
                                        donarViewHolder.setDonarpostset(donar_postget);
                                    }

                                    if(dataSnapshot.hasChild("donar_name")){
                                        String donar_nameget = dataSnapshot.child("donar_name").getValue().toString();
                                        donarViewHolder.setusernameset(donar_nameget);
                                    }
                                    if(dataSnapshot.hasChild("donar_location")){
                                        String donar_locationget = dataSnapshot.child("donar_location").getValue().toString();
                                        donarViewHolder.setLocationset(donar_locationget);
                                    }

                                    if(dataSnapshot.hasChild("login_name")){
                                        String login_nameget = dataSnapshot.child("login_name").getValue().toString();
                                        donarViewHolder.setusernameset(login_nameget);
                                    }

                                    donarViewHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String userid = dataSnapshot.child("UID").getValue().toString();

                                            if (userid.equals(CurrentUserID)) {
                                                donarViewHolder.chatimage.setVisibility(View.GONE);
                                            }
                                            else {
                                                donarViewHolder.chatimage.setVisibility(View.VISIBLE);
                                                android.app.AlertDialog.Builder Mbuilder = new android.app.AlertDialog.Builder(getContext());
                                                Mbuilder.setTitle("Select Options");

                                                CharSequence charSequence[] = new CharSequence[]{
                                                        "Send Message"
                                                };

                                                Mbuilder.setItems(charSequence, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if(which == 0){
                                                            String uid = dataSnapshot.child("UID").getValue().toString();
                                                            Intent intent = new Intent(getContext(), ChatActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            intent.putExtra("KEY", uid);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });

                                                AlertDialog alertDialog = Mbuilder.create();
                                                alertDialog.show();

                                            }

                                        }
                                    });




                                    donarViewHolder.likeicon.setOnClickListener(new View.OnClickListener() {
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

                                            LikeState = true;


                                            MLikeRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                  if(LikeState.equals(true)){
                                                      if(dataSnapshot.child(UID).hasChild(CurrentUserID)){
                                                          MLikeRef.child(UID).child(CurrentUserID).removeValue();
                                                          LikeState = false;
                                                      }
                                                      else {
                                                          MLikeRef.child(UID).child(CurrentUserID).setValue(true);
                                                          LikeState = false;
                                                      }
                                                  }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        }
                                    });


                                }
                                else {
                                    donarlayout.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
       // super.onStart();
    }

    public static class DonarViewHolder extends RecyclerView.ViewHolder{

        private View Mview;
        private CircleImageView profileiamge;
        private TextView username, bloodgroup;
        private TextView phonenumber, location, date;
        private Context context;
        private TextView donarpost;

        private ImageView likeicon;
        private TextView linketext;
        private int Likecounter;
        private InterstitialAd add;

        private DatabaseReference MLikeDatabase;
        private String CurrentUserID;

        private ImageView chatimage;
        private TextView loginname;


        public DonarViewHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            context = Mview.getContext();
            profileiamge = Mview.findViewById(R.id.DonarProfileimageID);
            username = Mview.findViewById(R.id.DonarUserNameID);
            bloodgroup = Mview.findViewById(R.id.DonarBloodGroupID);
            phonenumber = Mview.findViewById(R.id.DonarMobileNumberID);
            location = Mview.findViewById(R.id.DonarLocatinID);
            date = Mview.findViewById(R.id.DonarCurrentdateID);
            donarpost = Mview.findViewById(R.id.DonarPostID);

            likeicon = Mview.findViewById(R.id.LoveIconID);
            linketext = Mview.findViewById(R.id.LikeCounterIDID);

            MLikeDatabase = FirebaseDatabase.getInstance().getReference().child("DonarLike");
            CurrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            loginname = Mview.findViewById(R.id.CurrentUserNameBannerID);

            chatimage = Mview.findViewById(R.id.ChatdonarImageID);
        }

        public void setprofileimageset(String img){
            Picasso.with(context).load(img).placeholder(R.drawable.defaultimage).into(profileiamge);

            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(profileiamge, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }

        public void setusernameset(String nam){
            username.setText(nam);
        }

        public void setbloodgoupset(String group){
            bloodgroup.setText(group);
        }

        public void setPhonenumberset(String nuber){
            phonenumber.setText(nuber);
        }

        public void setLocationset(String loc){
            location.setText(loc);
        }

        public void setdateset(String dat){
            date.setText(dat);
        }

        public void setDonarpostset(String post){
            donarpost.setText(post);
        }

        private void setReadingLike(final String UID){

            MLikeDatabase = FirebaseDatabase.getInstance().getReference().child("DonarLike");
            MLikeDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(UID).hasChild(CurrentUserID)){
                        Likecounter =(int) dataSnapshot.child(UID).getChildrenCount();
                        likeicon.setImageResource(R.drawable.redlove);
                        linketext.setText(Integer.toString(Likecounter)+" Like");

                    }
                    else {
                        Likecounter = (int) dataSnapshot.child(UID).getChildrenCount();
                        likeicon.setImageResource(R.drawable.blacklove);
                        linketext.setText(Integer.toString(Likecounter)+ " Like");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
