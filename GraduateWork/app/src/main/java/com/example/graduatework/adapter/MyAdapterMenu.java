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
import com.example.graduatework.database.Category;
import java.util.ArrayList;

public class MyAdapterMenu extends RecyclerView.Adapter<MyAdapterMenu.ViewHolder> {

    Context context;
    ArrayList<Category> list;

    final View.OnClickListener onClickListener;

//    final View.OnClickListener onClickListener=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            int itemPosition = RecyclerView.getChildLayoutPosition(v);
//            String item = mList.get(itemPosition);
//            Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
//        }
//    };
//
//    public MyAdapterMenu(Context context, ArrayList<Category> list) {
//        this.context = context;
//        this.list = list;
//    }

    public MyAdapterMenu(Context context, ArrayList<Category> list, View.OnClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = list.get(position);
        holder.menuName.setText(category.getName());
        Glide.with(context).load(category.getImage()).into(holder.menuImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView menuName;
        ImageView menuImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            menuName = (TextView) itemView.findViewById(R.id.menuName);
            menuImage = (ImageView) itemView.findViewById(R.id.menuImage);
        }


    }

}
