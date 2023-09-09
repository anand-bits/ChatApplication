package com.example.chartapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {

    String recieverimg,recieverUid,receievrName, SenderUID;
    CircleImageView profile;
    TextView receiverNName;
    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    public static  String senderImg;

    public static  String receiveriImg;

    String senderRoom;
    String receiverRoom;
    RecyclerView messageAdapter;
    ArrayList<msgModel> messageArrayList;
    messageAdapter mmessagesAdapter;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        receievrName= getIntent().getStringExtra("namee");

        recieverimg=getIntent().getStringExtra("receiverimg");

        recieverUid=getIntent().getStringExtra("uid");

        messageArrayList= new ArrayList<>();

        sendbtn=findViewById(R.id.sendbtnn);

        textmsg= findViewById(R.id.writemsg);
        receiverNName=findViewById(R.id.receivername);
        profile=findViewById(R.id.profilrchat);



        messageAdapter= findViewById(R.id.msgadapter);



        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        messageAdapter.setLayoutManager(linearLayoutManager);

        mmessagesAdapter= new messageAdapter(chatWin.this,messageArrayList);
        messageAdapter.setAdapter(mmessagesAdapter);

        Picasso.get().load(recieverimg).into(profile);

        receiverNName.setText(""+receievrName);


        SenderUID= firebaseAuth.getUid();
        senderRoom=SenderUID+recieverUid;

        receiverRoom=recieverUid+SenderUID;



        DatabaseReference reference= database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatrefernce= database.getReference().child("chats").child(senderRoom).child("messages");
        chatrefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    msgModel message= dataSnapshot.getValue(msgModel.class);
                    messageArrayList.add(message);


                }
                mmessagesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // senderImg=snapshot.child("profilepic").getValue().toString();
                receiveriImg=recieverimg;



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= textmsg.getText().toString();
                 if(message.isEmpty())
                 {
                     Toast.makeText(chatWin.this, "Enter The text first",Toast.LENGTH_SHORT).show();

                 }
                 textmsg.setText("");
                 Date date= new Date();

                 msgModel messagess= new msgModel(message,SenderUID,date.getTime());
                 database= FirebaseDatabase.getInstance();
                 database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         database.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {

                             }
                         });
                     }
                 });






            }
        });








    }
}