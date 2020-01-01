package com.example.loginfirebase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextView txtDetails;
    private EditText inputName, inputEmail;
    private Button btnSave;
    DatabaseReference mFirebaseDatabase;
    FirebaseDatabase mFirebaseInstance;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        txtDetails = (TextView) findViewById(R.id.txt_user);
        inputName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        btnSave = (Button) findViewById(R.id.btn_save);

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
                String name = inputName.getText().toString();
                String email = inputEmail.getText().toString();
                if(TextUtils.isEmpty(userId))  //check exist or not = modileno
                {
                    createUser(name, email);  //empty = add, in future register
                }
                else
                {
                    updateUser(name, email);  //present = update, in future login
                }
            }
        });

        if(TextUtils.isEmpty(userId))
        {
            btnSave.setText("Save");
        }
        else
        {
            btnSave.setText("Update");
        }
    }

    private void createUser(String name, String email)
    {
        if(TextUtils.isEmpty(userId))
        {
            userId = mFirebaseDatabase.push().getKey();
        }
        User user = new User(name, email);
        mFirebaseDatabase.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(user==null)
                        {
                            Log.i("My user = ","null");
                            return;
                        }
                        Log.i("My user data is change=",user.name+", "+user.email);

                        //new updated name, email display
                        txtDetails.setText(user.name+", "+user.email);

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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("My Error onCancelled = ",databaseError+"");

                    }
                });

            }
        });
        //addUserChangeListener();
    }

    private void addUserChangeListener()
    {
        //user data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("My Error onCancelled = ",databaseError+"");
            }
        });
    }

    private void updateUser(String name, String email)  //update user via nodes
    {
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
