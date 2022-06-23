package com.example.smate.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.drm.DrmManagerClient;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smate.HostActivity;
import com.example.smate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class ValidateCode extends AppCompatActivity {

    String CodeBySystem,code;
    TextView etPassword2;
    ProgressBar progressBar;
    private String phone;
    OtpView otpView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_code);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        otpView = findViewById(R.id.etPassword2);

        String PhoneNumber = getIntent().getStringExtra("PhoneNumber");
        phone = PhoneNumber;
        sendVerificationCode(PhoneNumber);

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                code = otp;
                verifyCode(code);
            }
        });

    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                etPassword2.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ValidateCode.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            CodeBySystem = s;
        }
    };
    private void sendVerificationCode(String phoneNumber)
    {
        PhoneAuthOptions options= PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber("+91"+phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeBySystem,code);
        SignInUser(credential);
    }

    private void SignInUser(PhoneAuthCredential credential)
    {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isComplete())
                {
                    FirebaseUser user = task.getResult().getUser();
                    long creationTimestamp = user.getMetadata().getCreationTimestamp();
                    long lastSignInTimestamp = user.getMetadata().getLastSignInTimestamp();

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ValidateCode.this, "Verification complete", Toast.LENGTH_SHORT).show();

                    if (creationTimestamp+10 > lastSignInTimestamp) {
                        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                        intent.putExtra("phoneNumber",phone);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), HostActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
                else
                {
                    Toast.makeText(ValidateCode.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}