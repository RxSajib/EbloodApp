package com.blood.emirateslifedonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.blood.emirateslifedonation.Adapter.MessageAdapter;
import com.blood.emirateslifedonation.Common.Common;
import com.blood.emirateslifedonation.HolderClass.MessageHolder;
import com.blood.emirateslifedonation.Model.Myresponce;
import com.blood.emirateslifedonation.Model.Notification;
import com.blood.emirateslifedonation.Model.Sender;
import com.blood.emirateslifedonation.Model.Token;
import com.blood.emirateslifedonation.Remote.APIservice;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {


    private String ReciverID;
    private Toolbar toolbar;
    private DatabaseReference MuserDatabase;
    private TextView name;
    private CircleImageView profileimage;
    private TextView Messagetext;
    private ImageButton sendbutton;
    private String SenderID;
    private FirebaseAuth Mauth;
    private EditText message;
    private DatabaseReference Mroodref;
    private String CurrentTime, CurrentDate;

    private RecyclerView messagerecylearview;
    private List<MessageHolder> messageHolderList = new ArrayList<>();
    private DatabaseReference Messageref;
    private MessageAdapter messageAdapter;

    private DatabaseReference MMessageEdit;

    private ImageView send;
    private String SendType="";
    private Uri imageuri;
    private StorageReference Mimagestore;

    private DatabaseReference MOnlineStatasDatabase;

    private ImageView onlineimage;
    private TextView onlinetime;
    private APIservice mService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mService= Common.getFCMClient();
        onlineimage = findViewById(R.id.OnlineImage);
        onlinetime = findViewById(R.id.OnlineDate);

        Mimagestore = FirebaseStorage.getInstance().getReference();
        MOnlineStatasDatabase = FirebaseDatabase.getInstance().getReference().child("Statas");

        send = findViewById(R.id.PlusID);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence charSequence[] = new CharSequence[]{
                        "Send Image",
                        "Send Pdf",
                        "Send Doc"
                };

                AlertDialog.Builder Mbuilder  = new AlertDialog.Builder(ChatActivity.this);

                Mbuilder.setItems(charSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            SendType = "image";
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "select IMAGE"), 511);
                        }
                        if(which == 1){
                            SendType = "pdf";
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(Intent.createChooser(intent, "select PDF file"), 511);
                        }
                        if(which == 2){
                            SendType = "doc";
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(Intent.createChooser(intent, "select DOC file"), 511);
                        }
                    }
                });

                AlertDialog alertDialog = Mbuilder.create();
                alertDialog.show();

            }
        });

        MMessageEdit = FirebaseDatabase.getInstance().getReference().child("Edit_message");
        MMessageEdit.keepSynced(true);

        Messageref = FirebaseDatabase.getInstance().getReference();
        Messageref.keepSynced(true);
        messagerecylearview = findViewById(R.id.ChatListID);
        messagerecylearview.setHasFixedSize(true);
        messagerecylearview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        messageAdapter = new MessageAdapter(messageHolderList);
        messagerecylearview.setAdapter(messageAdapter);

        Mroodref = FirebaseDatabase.getInstance().getReference();
        Mroodref.keepSynced(true);
        Mauth = FirebaseAuth.getInstance();
        SenderID = Mauth.getCurrentUser().getUid();
        message = findViewById(R.id.MessageTextID);
        sendbutton = findViewById(R.id.MSendButtonID);



        profileimage = findViewById(R.id.PofileImageID);
        name = findViewById(R.id.usernameTID);
        ReciverID = getIntent().getStringExtra("KEY");
        toolbar = findViewById(R.id.ChatToolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(ReciverID);
        MuserDatabase.keepSynced(true);
        reading_uservalue();

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sending_Message();

            }
        });

        readingMessage();

        MMessageEdit
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("message")){
                                String messageget = dataSnapshot.child("message").getValue().toString();
                                message.setText(messageget);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        /// online stattas
        onlineStatas("online");
        readOnlineMood();
        /// online stattas
    }


    /// picup image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==511 && resultCode == RESULT_OK){
            imageuri = data.getData();
            if(SendType.equals("image")){
                StorageReference filepath = Mimagestore.child("send_image").child(imageuri.getLastPathSegment());
                filepath.putFile(imageuri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();

                                    String imagedownloaduri = task.getResult().getDownloadUrl().toString();

                                    String message_sender_ref = "Message/"+SenderID+"/"+ReciverID;
                                    String message_reciver_ref = "Message/"+ReciverID+"/"+SenderID;

                                    DatabaseReference user_message_key = Mroodref.child("Message").child(SenderID).child(ReciverID)
                                            .push();

                                    String message_push_id = user_message_key.getKey();

                                    Calendar calendartime = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm");
                                    CurrentTime = simpleDateFormattime.format(calendartime.getTime());

                                    Calendar calendardate = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("yyyy-MM-dd");
                                    CurrentDate = simpleDateFormatdate.format(calendardate.getTime());

                                    Map messagemap = new HashMap();
                                    messagemap.put("message", imagedownloaduri);
                                    messagemap.put("time", CurrentTime);
                                    messagemap.put("date", CurrentDate);
                                    messagemap.put("from", SenderID);
                                    messagemap.put("type", "image");

                                    Map messagebody = new HashMap();
                                    messagebody.put(message_sender_ref+"/"+message_push_id, messagemap);
                                    messagebody.put(message_reciver_ref+"/"+message_push_id, messagemap);

                                    message.setText("");
                                    Mroodref.updateChildren(messagebody)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if(task.isSuccessful()){

                                                        find_username_and_send_notification();
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });



                                }
                                else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            else if(!SendType.equals("image")){


                StorageReference filepath = Mimagestore.child("send_document").child(imageuri.getLastPathSegment());
                filepath.putFile(imageuri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){




                                    String imagedownloaduri = task.getResult().getDownloadUrl().toString();

                                    String message_sender_ref = "Message/"+SenderID+"/"+ReciverID;
                                    String message_reciver_ref = "Message/"+ReciverID+"/"+SenderID;

                                    DatabaseReference user_message_key = Mroodref.child("Message").child(SenderID).child(ReciverID)
                                            .push();

                                    String message_push_id = user_message_key.getKey();

                                    Calendar calendartime = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm");
                                    CurrentTime = simpleDateFormattime.format(calendartime.getTime());

                                    Calendar calendardate = Calendar.getInstance();
                                    SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("yyyy-MM-dd");
                                    CurrentDate = simpleDateFormatdate.format(calendardate.getTime());

                                    Map messagemap = new HashMap();
                                    messagemap.put("message", imagedownloaduri);
                                    messagemap.put("time", CurrentTime);
                                    messagemap.put("date", CurrentDate);
                                    messagemap.put("from", SenderID);
                                    messagemap.put("type", SendType);

                                    Map messagebody = new HashMap();
                                    messagebody.put(message_sender_ref+"/"+message_push_id, messagemap);
                                    messagebody.put(message_reciver_ref+"/"+message_push_id, messagemap);

                                    message.setText("");
                                    Mroodref.updateChildren(messagebody)
                                            .addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if(task.isSuccessful()){
                                                        find_username_and_send_notification();
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });



                                }
                                else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });


            }
            else {
                Toast.makeText(getApplicationContext(), "Error select object", Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /// picup image


    @Override
    protected void onDestroy() {
        onlineStatas("offline");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        onlineStatas("offline");
        super.onStop();
    }

    @Override
    protected void onStart() {
        onlineStatas("online");
        super.onStart();
    }

    /// online stats
    private void onlineStatas(String statas){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm");
        String OnlineTime =  simpleDateFormattime.format(calendar.getTime());

        Map onlinemap = new HashMap();
        onlinemap.put("statas", statas);
        onlinemap.put("last_seen", OnlineTime);


        MOnlineStatasDatabase.child(SenderID).updateChildren(onlinemap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){

                        }
                        else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //// send notifaction
    private void find_username_and_send_notification(){


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if(dataSnapshot.exists()){

                    String name =dataSnapshot.child("Name").getValue().toString();


                    if(!name.isEmpty()){


                        // Log.i(TAG, "onDataChange: ");

                        sendNotification(name);


                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    //// send notifaction


    /// send notifaction to another devices
    private void sendNotification(final String sendername) {



        FirebaseDatabase.getInstance().getReference("Token").child(ReciverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Token token=dataSnapshot.getValue(Token.class);


                String sendermessage= sendername;
                String title= message.getText().toString();


              /*  HashMap<String,String> data=new HashMap<>();
                data.put("title",title);
                data.put("body",message);*/


                Notification notification=new Notification(sendermessage,title);
                Sender noti=new Sender(token.getToken(),notification);


                mService.sendNotification(noti).enqueue(new Callback<Myresponce>() {
                    @Override
                    public void onResponse(Call<Myresponce> call, Response<Myresponce> response) {

                        Log.i("STATUS", "onResponse: SUCCESS "  +  response.message());

                    }

                    @Override
                    public void onFailure(Call<Myresponce> call, Throwable t) {

                        Log.i("STATUS", "onResponse: FAILED ");


                    }
                });







            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
    /// send notifaction to another devices


    /// online stats

    private void readOnlineMood(){
        MOnlineStatasDatabase.child(ReciverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("statas")){
                                String statasget = dataSnapshot.child("statas").getValue().toString();
                                if(statasget.equals("online")){
                                    onlineimage.setImageResource(R.drawable.online_dot);
                                    onlinetime.setVisibility(View.GONE);
                                }
                                if(statasget.equals("offline")){
                                    String last_seenget = dataSnapshot.child("last_seen").getValue().toString();
                                    onlineimage.setImageResource(R.drawable.offline_dot);
                                    onlinetime.setVisibility(View.VISIBLE);
                                }
                            }
                            if(dataSnapshot.hasChild("last_seen")){
                                String last_seenget = dataSnapshot.child("last_seen").getValue().toString();
                                onlinetime.setText(last_seenget);
                            }
                        }
                        else {
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void sending_Message(){

        String messagetext = message.getText().toString();
        if(messagetext.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please input any message", Toast.LENGTH_LONG).show();
        }
        else {


            String message_sender_ref = "Message/"+SenderID+"/"+ReciverID;
            String message_reciver_ref = "Message/"+ReciverID+"/"+SenderID;

            DatabaseReference user_message_key = Mroodref.child("Message").child(SenderID).child(ReciverID)
                    .push();

            String message_push_id = user_message_key.getKey();

            Calendar calendartime = Calendar.getInstance();
            SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("HH:mm");
            CurrentTime = simpleDateFormattime.format(calendartime.getTime());

            Calendar calendardate = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = simpleDateFormatdate.format(calendardate.getTime());

            Map messagemap = new HashMap();
            messagemap.put("message", messagetext);
            messagemap.put("time", CurrentTime);
            messagemap.put("date", CurrentDate);
            messagemap.put("from", SenderID);
            messagemap.put("type", "text");

            Map messagebody = new HashMap();
            messagebody.put(message_sender_ref+"/"+message_push_id, messagemap);
            messagebody.put(message_reciver_ref+"/"+message_push_id, messagemap);

            message.setText("");
            Mroodref.updateChildren(messagebody)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){

                                find_username_and_send_notification();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void reading_uservalue() {
        MuserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("Name")) {
                        String nameget = dataSnapshot.child("Name").getValue().toString();
                        name.setText(nameget);
                    }
                    if (dataSnapshot.hasChild("imageurl")) {
                        String imageurlget = dataSnapshot.child("imageurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageurlget).into(profileimage);
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void readingMessage(){
        Messageref.child("Message").child(SenderID).child(ReciverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        MessageHolder message = dataSnapshot.getValue(MessageHolder.class);
                        messageHolderList.add(message);
                        messageAdapter.notifyDataSetChanged();
                        messagerecylearview.smoothScrollToPosition(messageAdapter.getItemCount());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
