package com.example.graduatework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

public class FoodDetail extends AppCompatActivity {
    TextView foodName_det, foodPrice_det, foodAmount_det, foodDesc_det;
    ImageView foodImage_det;
    CollapsingToolbarLayout collapsing;
    FloatingActionButton btnCart;

    //elegant num btn
    TextView count_item;
    ImageView btnRemoveItem, btnAddItem;

    String foodId = "";

    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
    }
}