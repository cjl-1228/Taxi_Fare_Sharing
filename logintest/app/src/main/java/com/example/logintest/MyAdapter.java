package com.example.logintest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {



    Context context;
    ArrayList<Trip> list;

    public MyAdapter(Context context, ArrayList<Trip> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       Trip trip = list.get(position);
       holder.tripid.setText(trip.getTrip_id());
       holder.start.setText(trip.getStart());
       holder.end.setText(trip.getEnd());
       holder.money.setText(trip.getMoney());
       holder.people_count.setText(trip.getPeople_count());
       holder.join_people_count.setText(trip.getJoin_people_count());
       holder.datetime.setText(trip.getDatetime());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tripid, start, end, money, people_count, join_people_count, datetime, join_people;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tripid = itemView.findViewById(R.id.tvtripid);
            start = itemView.findViewById(R.id.tvstart);
            end = itemView.findViewById(R.id.tvend);
            money = itemView.findViewById(R.id.tvmoney);
            people_count = itemView.findViewById(R.id.tvpeople_count);
            join_people_count = itemView.findViewById(R.id.tvjoin_people_count);
            datetime = itemView.findViewById(R.id.tvdatetime);

        }
    }
}
