package com.example.loginfirebase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText inputName2, inputEmail2;
    private Button btnSave2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputName2 = (EditText) findViewById(R.id.name2);
        inputEmail2 = (EditText) findViewById(R.id.email2);
        btnSave2 = (Button) findViewById(R.id.btn_save2);
    }
}
