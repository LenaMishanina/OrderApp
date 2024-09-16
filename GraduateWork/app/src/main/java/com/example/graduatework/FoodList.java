package com.example.graduatework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.graduatework.Common.Common;
import com.example.graduatework.adapter.MyAdapterFood;
import com.example.graduatework.database.Category;
import com.example.graduatework.database.Database;
import com.example.graduatework.database.Food;
import com.example.graduatework.database.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FoodList extends AppCompatActivity {
    DatabaseReference database;
    RecyclerView recyclerFood;
    RecyclerView.LayoutManager layoutManager;

    MyAdapterFood myAdapterFood;
    ArrayList<Food> foodList;

    String categoryId = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        database = FirebaseDatabase.getInstance().getReference("Food");


        categoryId=getIntent().getStringExtra("CategoryId");
        Log.v("2 CATEGORYID", categoryId);



        //food
        recyclerFood = (RecyclerView) findViewById(R.id.recycleFood);
        recyclerFood.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerFood.setLayoutManager(layoutManager);






        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerFood.getChildLayoutPosition(v);
                Food item = foodList.get(itemPosition);
//                    Toast.makeText(FoodList.this, "FOOD"+ item.getName(), Toast.LENGTH_SHORT).show();

                Log.v("FOOD NAME CLICK", item.getName());

                database.orderByChild("Name").equalTo(item.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.v("I_M_HERE", "I M HERE");
                        //     Log.v("GET_KEY", snapshot.child(item.getName()).getRef().getKey());

                        String key = "no(";

                        for(DataSnapshot childSnapshot : snapshot.getChildren()){
                            Log.v("GET_KEY", childSnapshot.getKey());
                            key = childSnapshot.getKey();
                            Toast.makeText(FoodList.this, childSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                            Log.v("KEY FOOD in for", key);
                        }

                        Log.v("KEY FOOD", key);
                        Intent toFoodDetail=new Intent(FoodList.this, FoodDetail.class);
                        toFoodDetail.putExtra("FoodId", key);
                        startActivity(toFoodDetail);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        };


        foodList = new ArrayList<>();
        myAdapterFood=new MyAdapterFood(this,foodList,onClickListener);
        recyclerFood.setAdapter(myAdapterFood);



        //get data
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);

                    if (Objects.equals(food.getMenuId(), categoryId))
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