package com.example.loginfirebase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText inputName2, inputEmail2;
    private Button btnSave2, btn_back;
    TextView tv_error;
    DatabaseReference mFirebaseDatabase;
    FirebaseDatabase mFirebaseInstance;
    String name, email;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputName2 = (EditText) findViewById(R.id.name2);
        inputEmail2 = (EditText) findViewById(R.id.email2);
        btnSave2 = (Button) findViewById(R.id.btn_save2);
        btn_back = (Button) findViewById(R.id.btn_back);
        tv_error = (TextView) findViewById(R.id.tv_error);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        Log.i("My mFirebaseInstance = ",mFirebaseInstance+"");
        mFirebaseDatabase = mFirebaseInstance.getReference();
        Log.i("My reference = ",mFirebaseDatabase+"");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        btnSave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = inputName2.getText().toString();
                email = inputEmail2.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(name.equals(""))
                {
                    inputName2.setError("Username cannot be blank");
                    inputName2.setFocusable(true);
                }
                else if(name.length()<5)
                {
                    inputName2.setError("Username should be 5 character long");
                    inputName2.setFocusable(true);
                }
                else if(email.equals(""))
                {
                    inputEmail2.setError("Email cannot be blank");
                    inputEmail2.setFocusable(true);
                }
                else if (!email.matches(emailPattern))
                {
                    inputEmail2.setError("Invalid email address");
                    inputEmail2.setFocusable(true);
                }
                else
                {
                    //register by = unique name
                    //login by = check email
                    Log.i("My Login = ","done successfully");
                    userId = name;  //mobile no
                    mFirebaseDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("My response = ",dataSnapshot+"");
                            if(dataSnapshot.exists())  //name
                            {
                                //login done
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                tv_error.setError("Name or Email is invalid");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("My cancelled = ",databaseError.toString());
                        }
                    });
                }
            }
        });
    }
}
