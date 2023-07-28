package com.example.graduatework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        list = new ArrayList<>();

        loadOrders(Common.currentUser.getPhone());

    }

    private void loadOrders(String phone) {
        Log.v("ORDER LOAD", "I M IN ORDER LOAD");

        database.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.v("ORDER LOAD DATA CHANGE", "I M IN ORDER LOAD DATA CHANGE");
                for (DataSnapshot child : snapshot.getChildren()){
                    Log.v("ORDER LOAD FOR", "I M IN ORDER LOAD IN FOR");
                    Request request = child.getValue(Request.class);
                    list.add(request);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapterOrderStatus=new MyAdapterOrderStatus(this,list);
        recyclerView.setAdapter(myAdapterOrderStatus);

    }
}