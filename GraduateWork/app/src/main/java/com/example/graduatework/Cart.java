package com.example.graduatework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference database;

    TextView txtTotalPrice;
    Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance().getReference("Requests");

        txtTotalPrice =(TextView) findViewById(R.id.total);
        btnPlaceOrder=(Button)findViewById(R.id.btnPlaceOrder);

        recyclerView=(RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadListFood();

    }

    private void loadListFood(){

    }
}