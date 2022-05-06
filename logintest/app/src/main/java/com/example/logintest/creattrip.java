package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class creattrip extends AppCompatActivity implements View.OnClickListener,  DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-92b48-default-rtdb.firebaseio.com/");

    TextView time_btn;
    TextView date_btn;
    Calendar c = Calendar.getInstance();
    String date="";
    String time="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creattrip);
        Intent it = getIntent();
        String account = it.getStringExtra("帳號");
        String fullname = it.getStringExtra("名稱");
        String havetrip = it.getStringExtra("是否有車次");

        final EditText trip_id = findViewById(R.id.trip_id);
        final EditText start = findViewById(R.id.start);
        final EditText end = findViewById(R.id.end);
        final EditText people = findViewById(R.id.people);
        final EditText money = findViewById(R.id.money);
        time_btn = (TextView) findViewById(R.id.time);
        date_btn = (TextView) findViewById(R.id.date);
        final TextView back = findViewById(R.id.back);

        final Button creattripBtn = findViewById(R.id.creattripBtn);



        time_btn.setOnClickListener(this);
        date_btn.setOnClickListener(this);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(creattrip.this, MainActivity.class);
                it.putExtra("帳號",account);
                it.putExtra("名稱",fullname);
                startActivity(it);
                finish();
            }
        });




        creattripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String trip_idTxt = trip_id.getText().toString();
                final String startTxt = start.getText().toString();
                final String endTxt = end.getText().toString();
                final String peopleTxt= people.getText().toString();
                final String moneyTxt = money.getText().toString();


                if (date.isEmpty() || time.isEmpty() || trip_idTxt.isEmpty() || startTxt.isEmpty() || endTxt.isEmpty() || peopleTxt.isEmpty() || moneyTxt.isEmpty()){
                    Toast.makeText(creattrip.this, "請輸入所有欄位的資料!", Toast.LENGTH_LONG).show();
                }
                else{
                    databaseReference.child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(trip_idTxt) || havetrip.equals("yes")){
                                Toast.makeText(creattrip.this, "此車次已被使用或你已有車次!!!", Toast.LENGTH_LONG).show();

                            }
                            else{

                                databaseReference.child("trips").child(trip_idTxt).child("trip_id").setValue(trip_idTxt);
                                databaseReference.child("trips").child(trip_idTxt).child("start").setValue(startTxt);
                                databaseReference.child("trips").child(trip_idTxt).child("end").setValue(endTxt);
                                databaseReference.child("trips").child(trip_idTxt).child("people_count").setValue(peopleTxt);
                                databaseReference.child("trips").child(trip_idTxt).child("money").setValue(moneyTxt);
                                databaseReference.child("trips").child(trip_idTxt).child("datetime").setValue(date+"  "+time);
                                databaseReference.child("trips").child(trip_idTxt).child("join_people").child(fullname).setValue(fullname);
                                databaseReference.child("trips").child(trip_idTxt).child("join_people_count").setValue("1");
                                databaseReference.child("users").child(account).child("jointrip").setValue(trip_idTxt);

                                Toast.makeText(creattrip.this, "車次建立成功!!!", Toast.LENGTH_LONG).show();

                                Intent it = new Intent(creattrip.this, MainActivity.class);
                                it.putExtra("帳號",account);
                                it.putExtra("名稱",fullname);
                                startActivity(it);
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
    }

    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
       date_btn.setText("日期："+y+"/"+(m+1)+"/"+d);
       date=y+"/"+(m+1)+"/"+d;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int h, int m) {
        time_btn.setText("時間："+h+"："+m);
        time=h+":"+m;
    }


    @Override
    public void onClick(View v) {
        if (v == date_btn){
            new DatePickerDialog(this,this,c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        }
        if (v == time_btn){
            new TimePickerDialog(this,this,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
        }
    }
}