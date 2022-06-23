package com.example.smate.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class LoginActivity extends AppCompatActivity {

    OtpView etPhoneNumber;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        auth = FirebaseAuth.getInstance();


        etPhoneNumber.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Intent intent = new Intent(LoginActivity.this,ValidateCode.class);
                intent.putExtra("PhoneNumber",otp);
                startActivity(intent);
            }
        });
    }
}