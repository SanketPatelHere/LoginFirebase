package com.example.loginfirebase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textName, textEmail;
    FirebaseAuth mAuth;
    Button btn_logout;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference mFirebaseDatabase;
    FirebaseDatabase mFirebaseInstance;
    String userId;
    String name, email;
    String uid, phoneno;
    Uri photourl;
    boolean anonymous;
    PreferenceHelper myPref;
    ArrayList<User> lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.imageView);
        textName = findViewById(R.id.textViewName);
        textEmail = findViewById(R.id.textViewEmail);
        btn_logout = findViewById(R.id.btn_logout);

        FirebaseUser user = mAuth.getCurrentUser();
        Glide.with(this).load(user.getPhotoUrl()).into(imageView);
        textName.setText(user.getDisplayName());
        textEmail.setText(user.getEmail());
        Log.i("My user.getDisplayName",user.getDisplayName()+"");
        Log.i("My user.getEmail()",user.getEmail()+"");
        Log.i("My user.getMetadata()",user.getMetadata()+"");
        Log.i("My user.getProviderData",user.getProviderData()+"");
        Log.i("My user.getProviderId()",user.getProviderId()+"");
        Log.i("My user.getUid()",user.getUid()+"");
        Log.i("My user.isAnonymous()",user.isAnonymous()+"");
        Log.i("My user.getPhoneNumber",user.getPhoneNumber()+"");
        Log.i("My user.getPhotoUrl()",user.getPhotoUrl()+"");

        myPref = new PreferenceHelper(getApplicationContext());

        mFirebaseInstance = FirebaseDatabase.getInstance();
        Log.i("My mFirebaseInstance = ",mFirebaseInstance+"");
        mFirebaseDatabase = mFirebaseInstance.getReference();
        Log.i("My reference = ",mFirebaseDatabase+"");



        ////////////////////////////////////////
        //save data in firbase = name,email
        name = user.getDisplayName();
        email = user.getEmail();
        uid = user.getUid();
        anonymous = user.isAnonymous();
        phoneno = user.getPhoneNumber();
        photourl = user.getPhotoUrl();
        userId = name;  //mobile no

        User user1 = new User(name, email);
        //User user1 = new User(name, email, uid, phoneno, photourl, anonymous);
        lst = new ArrayList<>();
        lst.add(user1);
        myPref.saveArrayList(lst,"users");  //for save data in sp

        //for get data from sp
        if(myPref.getArrayList("users")==null)
        {
            Log.i("My Error in pref = ","not change list");
            Toast.makeText(this, "Data not saved in sp", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ArrayList<User> fetchList = myPref.getArrayList("users");
            Log.i("My fetchlist size = ",fetchList.size()+"");
            Log.i("My fetchlist = ",fetchList+"");
            lst = fetchList;
            Log.i("My lst size = ",lst.size()+"");
            Log.i("My lst name = ",lst.get(0).getName()+"");
            Toast.makeText(this, "Data saved in sp", Toast.LENGTH_SHORT).show();
        }

        mFirebaseDatabase.child("users").child(userId).setValue(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfileActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        mFirebaseDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("My response profile = ",dataSnapshot+"");
                if(dataSnapshot.exists())  //name
                {
                    //update
                    Toast.makeText(ProfileActivity.this, "Name already exist", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //add
                    //createUser(name, email);  //empty = add, in future register

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("My Error in = ","addListenerForSingleValueEvent"+databaseError);
            }
        });
        ////////////////////////////////////////


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for signout
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                setResult(MainActivity.RC_SIGN_IN);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null) //not logged in
        {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }
    }
}
