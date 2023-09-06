package com.example.chartapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {

    String recieverimg,recieverUid,receievrName, SenderUID;
    CircleImageView profile;
    TextView receiverNName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);

        receievrName= getIntent().getStringExtra("namee");
        recieverimg=getIntent().getStringExtra("receiverimg");
        recieverUid=getIntent().getStringExtra("uid");

        profile=findViewById(R.id.profilrchat);
        receiverNName=findViewById(R.id.receivername);

        Picasso.get().load(recieverimg).into(profile);
        receiverNName.setText(""+receievrName);








    }
}