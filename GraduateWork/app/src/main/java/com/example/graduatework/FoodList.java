package com.example.graduatework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.graduatework.adapter.MyAdapterFood;
import com.example.graduatework.adapter.MyAdapterMenu;
import com.example.graduatework.database.Category;
import com.example.graduatework.database.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodList extends AppCompatActivity {
    DatabaseReference database;
    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;

    MyAdapterFood myAdapterFood;
    ArrayList<Food> foodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database = FirebaseDatabase.getInstance().getReference("Food");


        //food
        recyclerMenu = (RecyclerView) findViewById(R.id.recycleFood);
        recyclerMenu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);

        foodList = new ArrayList<>();
        myAdapterFood=new MyAdapterFood(this,foodList);
        recyclerMenu.setAdapter(myAdapterFood);

        //get data
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
                myAdapterFood.notifyDataSetChanged();
                //        Toast.makeText(Home.this, categoryList.get(0).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}