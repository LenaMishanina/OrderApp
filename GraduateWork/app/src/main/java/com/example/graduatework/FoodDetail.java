package com.example.graduatework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatework.database.Database;
import com.example.graduatework.database.Food;
import com.example.graduatework.database.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {
    TextView foodName_det, foodPrice_det, foodAmount_det, foodDesc_det;
    ImageView foodImage_det;
    CollapsingToolbarLayout collapsing;
    FloatingActionButton btnCart;

    Food food;

    //elegant num btn
    TextView count_item;
    ImageView btnRemoveItem, btnAddItem;
    int totalItemCount=1;
    int totalPrice=0;

    String foodId = "";

    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database = FirebaseDatabase.getInstance().getReference("Food");


        //init
        foodName_det = (TextView) findViewById(R.id.foodName_det);
        foodPrice_det = (TextView) findViewById(R.id.foodPrice_det);
        foodAmount_det = (TextView) findViewById(R.id.foodAmount_det);
        foodDesc_det = (TextView) findViewById(R.id.foodDescription_det);
        foodImage_det = (ImageView) findViewById(R.id.img_food_det);
        collapsing =(CollapsingToolbarLayout) findViewById(R.id.collapsing);
        btnCart=(FloatingActionButton) findViewById(R.id.btnCart_det);
        count_item=(TextView) findViewById(R.id.count_of_item);
        btnRemoveItem= (ImageView) findViewById(R.id.btn_remove_item);
        btnAddItem= (ImageView) findViewById(R.id.btn_add_item);

        //get init
        if (getIntent()!=null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()){
            //get details
            database.child(foodId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Food food = snapshot.getValue(Food.class);
                    food = snapshot.getValue(Food.class);

                    //set
                    Picasso.with(getBaseContext()).load(food.getImage()).into(foodImage_det);

//                    collapsing.setTitle(food.getName());
                    foodName_det.setText(food.getName());
                    foodAmount_det.setText(food.getAmount());
                    foodDesc_det.setText(food.getDescription());
                    totalPrice = Integer.parseInt(food.getPrice()) * totalItemCount;
                    foodPrice_det.setText(String.valueOf(totalPrice));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalItemCount < 10){
                    totalItemCount++;
                    count_item.setText(String.valueOf(totalItemCount));
                    foodPrice_det.setText(String.valueOf(Integer.parseInt(food.getPrice()) * totalItemCount));
                }
            }
        });

        btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalItemCount > 1){
                    totalItemCount--;
                    count_item.setText(String.valueOf(totalItemCount));
                    totalPrice = Integer.parseInt(food.getPrice()) * totalItemCount;
                    foodPrice_det.setText(String.valueOf(totalPrice));

                }
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        food.getName(),
                        String.valueOf(totalItemCount),
                        String.valueOf(totalPrice),
                        food.getAmount()
                ));

                Toast.makeText(FoodDetail.this, "Added to Cart " + food.getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}