package com.example.graduatework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.graduatework.Common.Common;
import com.example.graduatework.adapter.MyAdapterOrderStatus;
import com.example.graduatework.database.Database;
import com.example.graduatework.database.Order;
import com.example.graduatework.database.Request;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference database;

    MyAdapterOrderStatus myAdapterOrderStatus;
    ArrayList<Request> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance().getReference("Requests");

        recyclerView=(RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        list = new ArrayList<>();
        myAdapterOrderStatus=new MyAdapterOrderStatus(this, list, onClickListener);
        recyclerView.setAdapter(myAdapterOrderStatus);


        loadOrders(Common.currentUser.getPhone(), onClickListener);

    }

    private void loadOrders(String phone, View.OnClickListener onClickListener) {
        Log.v("ORDER LOAD", "I M IN ORDER LOAD");

        database.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.v("ORDER LOAD DATA CHANGE", "I M IN ORDER LOAD DATA CHANGE");
                for (DataSnapshot child : snapshot.getChildren()){
                    Log.v("ORDER LOAD FOR", "I M IN ORDER LOAD IN FOR");
                    Request request = child.getValue(Request.class);
                    Log.v("REQUEST", request.getName() + ' ' + request.getAddress() + ' ' + request.getPhone());
                    list.add(request);
                }
                myAdapterOrderStatus.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myAdapterOrderStatus=new MyAdapterOrderStatus(this, list, onClickListener);
        recyclerView.setAdapter(myAdapterOrderStatus);

    }
}