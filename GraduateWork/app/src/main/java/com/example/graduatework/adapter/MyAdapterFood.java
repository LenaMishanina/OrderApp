package com.example.graduatework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduatework.R;
import com.example.graduatework.database.Food;

import java.util.ArrayList;

public class MyAdapterFood extends RecyclerView.Adapter<MyAdapterFood.ViewHolder> {

    Context context;
    ArrayList<Food> list;

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
        Food food = list.get(position);
        holder.foodName.setText(food.getName());
        holder.foodPrice.setText(food.getPrice());
        Glide.with(context).load(food.getImage()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView foodName, foodPrice;
        ImageView foodImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = (TextView) itemView.findViewById(R.id.foodName);
            foodPrice = (TextView) itemView.findViewById(R.id.foodPrice);
            foodImage = (ImageView) itemView.findViewById(R.id.foodImage);
        }
    }
}
