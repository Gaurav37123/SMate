package com.example.smate.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smate.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etPhoneNumber;
    Button btnSendOtp;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        auth = FirebaseAuth.getInstance();



        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ValidateCode.class);
                intent.putExtra("PhoneNumber",etPhoneNumber.getText().toString().trim());
                startActivity(intent);
            }
        });
    }
}