package com.example.graduatework.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduatework.Cart;
import com.example.graduatework.Common.Common;
import com.example.graduatework.FoodDetail;
import com.example.graduatework.FoodList;
import com.example.graduatework.R;
import com.example.graduatework.database.Database;
import com.example.graduatework.database.Food;
import com.example.graduatework.database.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterFood extends RecyclerView.Adapter<MyAdapterFood.ViewHolder> {

    Context context;
    ArrayList<Food> list;
    List<Order> cart = new ArrayList<>();

    final View.OnClickListener onClickListener;


    public MyAdapterFood(Context context, ArrayList<Food> list, View.OnClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.onClickListener = onClickListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Food");

        Food food = list.get(position);
        holder.foodName.setText(food.getName());
        holder.foodPrice.setText(food.getPrice());
        Glide.with(context).load(food.getImage()).into(holder.foodImage);

        holder.btnAddCartQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cart = new Database(v.getContext()).getCarts();
                int counter = 0;
                for (Order order : cart) {
                    if (order.getProductName().equals(food.getName().toString())){
                        counter++;
                    }
                }

                if (counter > 0){
                    Intent intent = new Intent(context, Cart.class);
                    context.startActivity(intent);
                } else {


                    database.orderByChild("Name").equalTo(food.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.v("I_M_HERE", "I M HERE");
                            //     Log.v("GET_KEY", snapshot.child(item.getName()).getRef().getKey());

                            String key = "no(";

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                Log.v("GET_KEY", childSnapshot.getKey());
                                key = childSnapshot.getKey();
                                Toast.makeText(v.getContext(), childSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                Log.v("KEY FOOD in for", key);
                            }
                            new Database(v.getContext()).addToCart(new Order(
                                    key,
                                    food.getName(),
                                    "1",
                                    food.getPrice(),
                                    food.getAmount(),
                                    food.getImage()
                            ));

                            Toast.makeText(v.getContext(), "Added to Cart " + food.getName(), Toast.LENGTH_SHORT).show();

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
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView foodName, foodPrice;
        ImageView foodImage, btnAddCartQuick;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = (TextView) itemView.findViewById(R.id.foodName);
            foodPrice = (TextView) itemView.findViewById(R.id.foodPrice);
            foodImage = (ImageView) itemView.findViewById(R.id.foodImage);
            btnAddCartQuick = (ImageView) itemView.findViewById(R.id.btn_add_to_cart_quick);

        }
    }
}
