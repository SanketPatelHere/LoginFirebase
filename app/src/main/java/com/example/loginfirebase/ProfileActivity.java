package com.example.loginfirebase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textName, textEmail;
    FirebaseAuth mAuth;
    Button btn_logout;
    GoogleSignInClient mGoogleSignInClient;

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
