package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-92b48-default-rtdb.firebaseio.com/");
    String havetrip="no" ;
    String id;
    String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent it = getIntent();
        String account = it.getStringExtra("帳號");
        String fullname = it.getStringExtra("名稱");
        TextView welcomeuser = findViewById(R.id.welcomeuser);
        welcomeuser.setText("哈囉，"+fullname);

        Button creatBtn = findViewById(R.id.creatBtn);
        Button joinBtn = findViewById(R.id.joinBtn);
        Button quitBtn = findViewById(R.id.quitBtn);
        Button seeBtn = findViewById(R.id.seeBtn);
        TextView logoutBtn = findViewById(R.id.logoutbtn);

        TextView outputtripid = findViewById(R.id.outputtripid);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "登出成功!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });




        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (havetrip.equals("no")){
                    Toast.makeText(MainActivity.this, "您沒加入車次!", Toast.LENGTH_LONG).show();
                }
                else{

                    databaseReference.child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int countnow = Integer.parseInt(snapshot.child(id).child("join_people_count").getValue(String.class));
                            countnow-=1;
                            String countnowTxt = String.valueOf(countnow);
                            databaseReference.child("trips").child(id).child("join_people_count").setValue(countnowTxt);
                            databaseReference.child("users").child(account).child("jointrip").removeValue();
                            databaseReference.child("trips").child(id).child("join_people").child(fullname).removeValue();
                            havetrip="no";
                            outputtripid.setText("找人分擔計程車錢嗎?  快進去看看!");
                            Toast.makeText(MainActivity.this, "成功退出車次!", Toast.LENGTH_LONG).show();

                            int c = Integer.parseInt(countnowTxt);
                            if(c==0){
                                databaseReference.child("trips").child(id).removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            }
        });

        creatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, creattrip.class);
                it.putExtra("帳號",account);
                it.putExtra("名稱",fullname);
                it.putExtra("是否有車次",havetrip);
                startActivity(it);
                finish();
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, jointrip.class);
                it.putExtra("帳號",account);
                it.putExtra("名稱",fullname);
                it.putExtra("是否有車次",havetrip);
                startActivity(it);
                finish();
            }
        });

        seeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, seetrip.class);
                it.putExtra("帳號",account);
                it.putExtra("名稱",fullname);

                startActivity(it);
                finish();
            }
        });

        databaseReference.child("users").child(account).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("jointrip")) {
                    id = snapshot.child("jointrip").getValue(String.class);
                    outputtripid.setText("你加入的車次："+id);
                    havetrip = "yes";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}