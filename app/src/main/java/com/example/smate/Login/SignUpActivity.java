
package com.example.smate.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smate.Classes.UserProfile;
import com.example.smate.HostActivity;
import com.example.smate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("user");
    private Button btnSignUp;
    private TextInputEditText etUsn,etName;
    private TextInputLayout T1,T2;
    UserProfile userData;
    private String branch,year,name,usn;
    private Spinner SpinnerBranch,SpinnerYear;
    ArrayList<String> YearItem = new ArrayList<>();
    ArrayList<String> BranchItem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnSignUp = findViewById(R.id.btnSignUp);
        etUsn = findViewById(R.id.etUsn);
        etName = findViewById(R.id.etName);
        T1 = findViewById(R.id.TI1);
        T2 = findViewById(R.id.TI2);
        SpinnerBranch = findViewById(R.id.SpinnerBranch);
        SpinnerYear = findViewById(R.id.SpinnerYear);
//        SpinnerBranch.setSelection(0);
//        SpinnerYear.setSelection(0);
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=2000;i<=curYear;i++)
        {
            YearItem.add(""+i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,YearItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerYear.setAdapter(adapter);
        SpinnerYear.setSelection(0);
        Collections.addAll(BranchItem,getResources().getStringArray(R.array.branch_list));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branch = SpinnerBranch.getSelectedItem().toString().trim();
                year = SpinnerYear.getSelectedItem().toString().trim();
                name = etName.getText().toString().trim();
                usn = etUsn.getText().toString().trim();
                if(name.isEmpty()||usn.isEmpty())
                {
                    if(name.isEmpty())
                    {
                        T2.setError("Enter this field");
                    }
                    if(usn.isEmpty()) {
                        T1.setError("Enter this field");
                    }
                }
                else {
                    String phone = getIntent().getStringExtra("phoneNumber");
                    userData = new UserProfile(phone,usn,name,branch,year);
                    SignUp();
                }

            }
        });
    }
    private void SignUp()
    {
        dbRef.child(user.getPhoneNumber()).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, HostActivity.class);
                    startActivity(intent);
                    SignUpActivity.this.finish();
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}