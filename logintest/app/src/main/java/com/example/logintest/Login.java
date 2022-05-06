package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.text.SimpleDateFormat;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-92b48-default-rtdb.firebaseio.com/");

    TextView Nowdatetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText account = findViewById(R.id.account);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);



        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Nowdatetime = (TextView)findViewById(R.id.Nowdatetime);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d ah:m:s ");
                                String dateString = sdf.format(date);
                                Nowdatetime.setText(dateString);
                            }
                        });
                    }
                }catch (InterruptedException e){
                }
            }
        };
        t.start();



        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final String accountTxt = account.getText().toString();
                final String PasswordTxt = password.getText().toString();

                if (accountTxt.isEmpty() || PasswordTxt.isEmpty()){
                    Toast.makeText(Login.this, "請輸入帳號或密碼!", Toast.LENGTH_LONG).show();
                }
                else{
                   databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.hasChild(accountTxt)){
                               final String getPassword = snapshot.child(accountTxt).child("password").getValue(String.class);

                               if (getPassword.equals(PasswordTxt)){
                                   Toast.makeText(Login.this, "登入成功!", Toast.LENGTH_LONG).show();
                                   Intent it = new Intent(Login.this, MainActivity.class);
                                   it.putExtra("帳號",accountTxt);
                                   it.putExtra("名稱",snapshot.child(accountTxt).child("fullname").getValue(String.class));
                                   startActivity(it);
                                   finish();

                               }
                               else{
                                   Toast.makeText(Login.this, "密碼錯誤!", Toast.LENGTH_LONG).show();
                               }
                           }
                           else{
                               Toast.makeText(Login.this, "密碼錯誤!", Toast.LENGTH_LONG).show();
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
                }

            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }





}