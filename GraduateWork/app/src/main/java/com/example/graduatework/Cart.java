package com.example.graduatework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.graduatework.adapter.MyAdapterCart;
import com.example.graduatework.database.Database;
import com.example.graduatework.database.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference database;

    TextView txtTotalPrice;
    Button btnPlaceOrder;

    List<Order> cart = new ArrayList<>();
    MyAdapterCart adapter;

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
        cart = new Database(this).getCarts();
        adapter=new MyAdapterCart(cart,this);
        recyclerView.setAdapter(adapter);

        //total price
        int total = 0;
        for (Order order : cart) {
            total += ( (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity())) );
        }

        txtTotalPrice.setText(String.valueOf(total));
    }
}