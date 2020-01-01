package com.example.loginfirebase;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;
    Button btn_login;

    //for google sign in
    public static final int RC_SIGN_IN = 234;
    public static GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*FirebaseDatabase.getInstance().setPersistenceEnabled(true); //for offline data store
        reference = FirebaseDatabase.getInstance().getReference();
        Log.i("My reference = ",reference+"");  //give url of firebase database*/
        //My reference =: https://loginfirebase-195e8.firebaseio.com

        //DatabaseReference ref = (DatabaseReference) reference.getRef("copyright");
        //ref.setValue("@2016 androidhive. All right reserved");

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        Log.i("My users url = ",mDatabase+"");
        //String userId2 = mDatabse.getKey();
        //final String userId = mDatabase.push().getKey();
        final String userId = "9274467153"; //txtemail
        //Log.i("My usersId2 = ",userId2+"");  //users
        Log.i("My usersId = ",userId+""); //-LxUXrrOGRf2nLDIzahy, -LxUY43bMR57Py8f3CnJ


        //insert data
        User user = new User("Sanket2","Sanketramani0@gmail.com");
        //mDatabase.child(userId).setValue(user);  //for latest data get = add listener
        mDatabase.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //read data
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            Log.i("My snapshot = ",snapshot+"");
                            //User user1 = snapshot.getValue(User.class);
                            //Log.i("My user1 : Name = ",user1.getName()+", Email = "+user1.getEmail());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("My Error onCancelled = ",databaseError+"");
                    }
                });
            }
        }); //add with id

        //updateting data  //userId = 97230312228, 9274467153, new
        String newEmail = "Sanketramani10@gmail.com";  //9274467153 change
        mDatabase.child(userId).child("email").setValue(newEmail);

        //deleting data
        //mDatabase.child(userId).child("email").setValue(null);  //only email
        //mDatabase.child(userId).setValue(null);  //whole object
        //or
        //mDatabase.child(userId).child("email").removeValue();  //only email
        //mDatabase.child(userId).removeValue();  //whole object










        //for login demo
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });




        //for google signin
        mAuth = FirebaseAuth.getInstance();
        Log.i("My Firebase mAuth = ",mAuth+"");
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        //default_web_client_id = 2131623989
        Log.i("My default_webclientid=",R.string.default_web_client_id+"");
        Log.i("My gso=",gso+"");
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.i("My mGoogleSignInClient=",mGoogleSignInClient+"");

        findViewById(R.id.sing_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("Loading...");
                pd.show();
            }
        });

    }
    private void signIn()
    {
        //get googlsignin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.i("My signInIntent = ",signInIntent+"");
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("My onActivityResult1 = "," inside");
        if(requestCode==RC_SIGN_IN)
        {
            Log.i("My onActivityResult2 = ",data+"");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.i("My Task = ",task.getResult()+"");
            try
            {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i("My account = ",account.getEmail()+"");
                firebaseAuthWithGoogle(account);


            }
            catch (Exception e)
            {
                Log.i("My Error in onActivity=",e+"");
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount act)
    {
        Log.i("My firebaseAuthWith id=",act.getId());
        //get auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(act.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.i("My signInWithCredent"," = success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i("My FirebaseUser = ",user+"");
                            Toast.makeText(MainActivity.this, "Google Signin done successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                            pd.dismiss();
                            finish();
                        }
                        else
                        {
                            Log.i("My signInWithCredent"," = failure"+task.getException());
                            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            boolean connected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
                             if(!connected)
                             {
                                 Toast.makeText(MainActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                             }
                             else
                             {
                                 Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                             }
                            pd.dismiss();

                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        //if already signin = close current activity, go to profile activity
        if(mAuth.getCurrentUser()!=null)
        {
            Log.i("My current user = ",mAuth.getCurrentUser().getDisplayName()+"");
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            finish();
        }
    }


}
