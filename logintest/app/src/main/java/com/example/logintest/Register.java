package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class Register extends AppCompatActivity {


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-92b48-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fullname = findViewById(R.id.fullname);
        final EditText account = findViewById(R.id.account);
        final EditText password = findViewById(R.id.password);
        final EditText conpassword = findViewById(R.id.conPassword);
        final EditText email = findViewById(R.id.email);

        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNowBtn = findViewById(R.id.loginNow);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String fullnameTxt = fullname.getText().toString();
                final String accountTxt = account.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conpasswordTxt = conpassword.getText().toString();
                final String emailTxt = email.getText().toString();

                if (fullnameTxt.isEmpty() || accountTxt.isEmpty() || passwordTxt.isEmpty() || conpasswordTxt.isEmpty() || emailTxt.isEmpty()){
                    Toast.makeText(Register.this, "請輸入所有欄位的資料!", Toast.LENGTH_LONG).show();
                }
                else if (!passwordTxt.equals(conpasswordTxt)){
                    Toast.makeText(Register.this, "密碼不一致!", Toast.LENGTH_LONG).show();
                }
                else{

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(accountTxt)){
                                Toast.makeText(Register.this, "此帳號已使用!!!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                databaseReference.child("users").child(accountTxt).child("fullname").setValue(fullnameTxt);
                                databaseReference.child("users").child(accountTxt).child("account").setValue(accountTxt);
                                databaseReference.child("users").child(accountTxt).child("password").setValue(passwordTxt);
                                databaseReference.child("users").child(accountTxt).child("email").setValue(emailTxt);

                                Toast.makeText(Register.this, "帳號建立成功.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}