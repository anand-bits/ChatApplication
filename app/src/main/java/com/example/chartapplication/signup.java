package com.example.chartapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class signup extends AppCompatActivity {
    TextView loginbut;
    String emailpattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Button rt_signup;
    EditText rt_email,rt_password,rt_renterpassword,rt_username;
    CircleImageView rt_profile;
    Uri imageURI;
    String imageuri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Establishing Ur account...");
        progressDialog.setCancelable(false);


        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        loginbut=findViewById(R.id.rtlogin);
        rt_username=findViewById(R.id.rtusername);
        rt_email=findViewById(R.id.rtemail);
        rt_password=findViewById(R.id.rtpassword);
        rt_renterpassword=findViewById(R.id.rtrenterpassword);
        rt_profile=findViewById(R.id.profile_image);
        rt_signup=findViewById(R.id.buttonsignup);

        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(signup.this,login.class);
                startActivity(intent);
                finish();
            }
        });
        rt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee=rt_username.getText().toString();
                String emaill= rt_email.getText().toString();
                String Password= rt_password.getText().toString();
                String cpassword= rt_renterpassword.getText().toString();
                String status= "Hey I'm using this Application";

                if(TextUtils.isEmpty(namee)|| TextUtils.isEmpty(emaill)
                        || TextUtils.isEmpty(Password)|| TextUtils.isEmpty(cpassword))
                {
                    progressDialog.dismiss();
                    Toast.makeText(signup.this,"Please Enter Valid  Information",Toast.LENGTH_SHORT).show();
                }
                else if(!emaill.matches(emailpattern))
                {
                    progressDialog.dismiss();
                    rt_email.setError("Type a valid error");
                }
                else if(Password.length()<6)
                {  progressDialog.dismiss();
                    rt_password.setError("Password must be of 6 character or more");
                }
                else if (!Password.equals(cpassword))
                {  progressDialog.dismiss();
                    rt_password.setError("Password don't match");
                }
                else
                {
                    auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                String id= task.getResult().getUser().getUid();
                                DatabaseReference reference= database.getReference().child("user").child(id);
                                StorageReference storageReference=storage.getReference().child("Upload").child(id);

                                if(imageURI!=null)
                                {
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri=uri.toString();
                                                        User users= new User(imageuri,emaill,namee,Password,id,status);
                                                        // this.profilepoc = profilepoc;
                                                        //        this.mail = mail;
                                                        //        this.userName = userName;
                                                        //        this.password = password;
                                                        //        this.userId = userId;
                                                        //        this.status = status;
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {  progressDialog.show();
                                                                    Intent intent= new Intent(signup.this,MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();

                                                                }
                                                                else {

                                                                    Toast.makeText(signup.this,"Error in creating user",Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });

                                                    }
                                                });
                                            }

                                        }
                                    });
                                }
                                else {
                                    String status="Hey i m using This Application";
                                    imageuri="https://firebasestorage.googleapis.com/v0/b/chartapplication-61ba4.appspot.com/o/Anand.jpg?alt=media&token=3ff7e4f9-c582-4d35-ab32-ff6dd10be25b";
                                    User user= new User(imageuri,emaill,namee,Password,id,status);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Intent intent= new Intent(signup.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                            else {

                                                Toast.makeText(signup.this,"Error in creating user",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }

                            }
                            else {
                                Toast.makeText(signup.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


        rt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select The picture"),10);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10) {
            if (data != null) {
                imageURI = data.getData();
                rt_profile.setImageURI(imageURI);
            }
        }
    }
}