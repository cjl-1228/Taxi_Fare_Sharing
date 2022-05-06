package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

public class seetrip extends AppCompatActivity implements DialogInterface.OnClickListener{

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-92b48-default-rtdb.firebaseio.com/");

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<Trip> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seetrip);

        Intent it = getIntent();
        String account = it.getStringExtra("帳號");
        String fullname = it.getStringExtra("名稱");

        TextView back = (TextView) findViewById(R.id.back);
        TextView searchtripBtn = (TextView) findViewById(R.id.searchtripBtn);
        EditText searchtrip = (EditText)findViewById(R.id.searchtrip);


        searchtripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchtripTxt = searchtrip.getText().toString();
                if (searchtripTxt.isEmpty()){
                    Toast.makeText(seetrip.this, "請輸入車次!", Toast.LENGTH_LONG).show();
                }
                else {
                    databaseReference.child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(searchtripTxt)){

                                String trip_id = snapshot.child(searchtripTxt).child("trip_id").getValue(String.class);
                                String start = snapshot.child(searchtripTxt).child("start").getValue(String.class);
                                String end = snapshot.child(searchtripTxt).child("end").getValue(String.class);
                                String money = snapshot.child(searchtripTxt).child("money").getValue(String.class);
                                String people_count = snapshot.child(searchtripTxt).child("people_count").getValue(String.class);
                                String join_people_count = snapshot.child(searchtripTxt).child("join_people_count").getValue(String.class);
                                String datetime = snapshot.child(searchtripTxt).child("datetime").getValue(String.class);

                                AlertDialog.Builder dialog = new AlertDialog.Builder(seetrip.this);
                                  dialog.setMessage(
                                          "車次："+trip_id+'\n'+
                                          "起點："+start+'\n'+
                                          "終點："+end+'\n'+
                                          "人數："+people_count+'\n'+
                                          "參加人數："+join_people_count+'\n'+
                                          "金額："+money+'\n'+
                                          "時間："+datetime+'\n'
                                  );
                                  dialog.setTitle("成功找到車次!");
                                  dialog.setPositiveButton("確定",seetrip.this);
                                  dialog.show();
                                  searchtrip.setText("");
                            }
                            else{
                                Toast.makeText(seetrip.this, "無車次!", Toast.LENGTH_LONG).show();
                                searchtrip.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(seetrip.this, MainActivity.class);
                it.putExtra("帳號",account);
                it.putExtra("名稱",fullname);
                startActivity(it);
                finish();
            }
        });

        recyclerView = findViewById(R.id.tripList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        databaseReference.child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Trip trip = dataSnapshot.getValue(Trip.class);
                    list.add(trip);

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });












    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {

    }
}