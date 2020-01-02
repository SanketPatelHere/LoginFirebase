package com.example.loginfirebase;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private TextView txtDetails;
    private EditText inputName, inputEmail;
    private Button btnSave;
    DatabaseReference mFirebaseDatabase;
    FirebaseDatabase mFirebaseInstance;
    String userId;
    Button btn_mylogin;
    String name, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        txtDetails = (TextView) findViewById(R.id.txt_user);
        inputName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        btnSave = (Button) findViewById(R.id.btn_save);
        btn_mylogin = (Button) findViewById(R.id.btn_mylogin);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        Log.i("My mFirebaseInstance = ",mFirebaseInstance+"");
        mFirebaseDatabase = mFirebaseInstance.getReference();
        Log.i("My reference = ",mFirebaseDatabase+"");
        FirebaseDatabase.getInstance().getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("My app title = ","updated");
                Log.i("My dataSnapshot = ",dataSnapshot+"");
                String appTitle = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("My app title = ","not updated"+databaseError);

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = inputName.getText().toString();
                email = inputEmail.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(name.equals(""))
                {
                    inputName.setError("Username cannot be blank");
                    inputName.setFocusable(true);
                }
                else if(name.length()<5)
                {
                    inputName.setError("Username should be 5 character long");
                    inputName.setFocusable(true);
                }
                else if(email.equals(""))
                {
                    inputEmail.setError("Email cannot be blank");
                    inputEmail.setFocusable(true);
                }
                else if (!email.matches(emailPattern))
                {
                    inputEmail.setError("Invalid email address");
                    inputEmail.setFocusable(true);
                }


                else
                {



                    userId = name;  //mobile no
                    mFirebaseDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //Log.i("My onDataChange in = ",dataSnapshot+"");
                                    //Log.i("My email check = ",dataSnapshot.getValue()+"");
                                    if(dataSnapshot.exists())  //name
                                    {
                                        //update
                                        Toast.makeText(RegisterActivity.this, "Name already exist", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        //add
                                        createUser(name, email);  //empty = add, in future register

                                    }
                                                /*for(DataSnapshot snapshot : dataSnapshot.getChildren() )
                                                {
                                                    //Log.i("My snapshot2",snapshot+"");
                                                    User user1 = snapshot.getValue(User.class);
                                                    Log.i("My emails",user1.getEmail()+"");
                                                    boolean s = user1.getEmail().equalsIgnoreCase(email);
                                                    if(s)
                                                    {
                                                        Log.i("My email = ","exists=register");
                                                        Toast.makeText(RegisterActivity.this, "Emailid already exists", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else if(!s)
                                                    {
                                                        Log.i("My email = ","not exists=login");
                                                        //Toast.makeText(RegisterActivity.this, "Go for register", Toast.LENGTH_SHORT).show();
                                                        //createUser(name, email);  //empty = add, in future register


                                                        *//*if(TextUtils.isEmpty(userId))  //check exist or not = modileno
                                                        {
                                                            createUser(name, email);  //empty = add, in future register
                                                        }
                                                        else
                                                        {
                                                            updateUser(name, email);  //present = update, in future login
                                                        }*//*
                                                    }
                                                }*/

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.i("My Error in = ","addListenerForSingleValueEvent"+databaseError);
                                }
                            });




                }


            }
        });
        //refreshUser();



        if(TextUtils.isEmpty(userId))
        {
            btnSave.setText("Register");
        }
        else
        {
            btnSave.setText("Update");

        }

        btn_mylogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void createUser(String name, String email2)
    {
        Log.i("My createUser = ",name+", "+email2);
        /*if(TextUtils.isEmpty(userId))
        {
            //userId = mFirebaseDatabase.push().getKey();
        }*/

        

        User user = new User(name, email2);
        mFirebaseDatabase.child("users").child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        refreshUser();
        //addUserChangeListener();
    }


    public void refreshUser()
    {
        if(userId!="")
        {

        mFirebaseDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    User user = dataSnapshot.getValue(User.class);
                    Log.i("My user data change1=",user.name+", "+user.email);
                    //new updated name, email display
                    txtDetails.setText(user.name+", "+user.email);
                    Toast.makeText(RegisterActivity.this, "Registration Successfull for "+user.email+" user", Toast.LENGTH_SHORT).show();
                    //clear edittext
                    inputName.setText("");
                    inputEmail.setText("");
                    if(TextUtils.isEmpty(userId))
                    {
                        btnSave.setText("Save");
                    }
                    else
                    {
                        btnSave.setText("Update");
                    }
                    Log.i("My a = ","aaaaaaa");
                }
                else
                {
                    Log.i("My b = ","bbbbbb");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("My Error onCancelled = ","on name changed = "+databaseError);

            }
        });
        }
        else
        {
            txtDetails.setText("Register Yourself");
        }
    }
    private void updateUser(String name, String email)  //update user via nodes
    {
        Log.i("My updateUser = ",name+", "+email);
        if(!TextUtils.isEmpty(name))  //already present + textview not empty
        {
            mFirebaseDatabase.child(userId).child("name").setValue(name);
        }
        if(!TextUtils.isEmpty(email))
        {
            mFirebaseDatabase.child(userId).child("email").setValue(email);
        }
    }
}
