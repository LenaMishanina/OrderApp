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

    public MyAdapterFood(Context context, ArrayList<Food> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = list.get(position);
        holder.foodName.setText(food.getName());
        Glide.with(context).load(food.getImage()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView foodName;
        ImageView foodImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = (TextView) itemView.findViewById(R.id.foodName);
            foodImage = (ImageView) itemView.findViewById(R.id.foodImage);
        }
    }
}
