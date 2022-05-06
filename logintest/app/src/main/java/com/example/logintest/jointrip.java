package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class jointrip extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-92b48-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jointrip);

        Intent it = getIntent();
        String account = it.getStringExtra("帳號");
        String fullname = it.getStringExtra("名稱");
        String havetrip = it.getStringExtra("是否有車次");

        Button addBtn = (Button) findViewById(R.id.addBtn);
        TextView back = (TextView) findViewById(R.id.back);
        EditText search = (EditText) findViewById(R.id.search);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(jointrip.this, MainActivity.class);
                it.putExtra("帳號",account);
                it.putExtra("名稱",fullname);

                startActivity(it);
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTxt = search.getText().toString();
                if (searchTxt.isEmpty()){
                    Toast.makeText(jointrip.this, "請輸入車次!", Toast.LENGTH_LONG).show();
                }
                else{

                    databaseReference.child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(searchTxt) && havetrip.equals("no") ) {

                                int countnow = Integer.parseInt(snapshot.child(searchTxt).child("join_people_count").getValue(String.class));
                                int count = Integer.parseInt(snapshot.child(searchTxt).child("people_count").getValue(String.class));
                                countnow+=1;
                                String countnowTxt = String.valueOf(countnow);

                                if (countnow<=count){
                                    databaseReference.child("trips").child(searchTxt).child("join_people").child(fullname).setValue(fullname);
                                    databaseReference.child("users").child(account).child("jointrip").setValue(searchTxt);
                                    databaseReference.child("trips").child(searchTxt).child("join_people_count").setValue(countnowTxt);

                                    Toast.makeText(jointrip.this, "成功加入車次!", Toast.LENGTH_LONG).show();
                                    Intent it = new Intent(jointrip.this, MainActivity.class);
                                    it.putExtra("帳號",account);
                                    it.putExtra("名稱",fullname);
                                    startActivity(it);
                                    finish();
                                }
                                else{
                                    Toast.makeText(jointrip.this, "人數已滿!", Toast.LENGTH_LONG).show();
                                    search.setText("");
                                }



                            }
                            else{
                                Toast.makeText(jointrip.this, "無此車次或已有車次!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }
}