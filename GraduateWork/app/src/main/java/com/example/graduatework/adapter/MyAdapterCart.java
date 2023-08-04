package com.example.graduatework.adapter;



import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduatework.Cart;
import com.example.graduatework.Common.Common;
import com.example.graduatework.Common.ItemClickListener;
import com.example.graduatework.R;
import com.example.graduatework.database.Database;
import com.example.graduatework.database.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    public TextView txt_cart_name, txt_cart_price, txt_cart_count;
    public ImageView btnAdd, btnRemove, cart_image;
    private ItemClickListener itemClickListener;


    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
        txt_cart_price = (TextView) itemView.findViewById(R.id.cart_item_price);
        txt_cart_count = (TextView) itemView.findViewById(R.id.cart_item_count);
        btnAdd = (ImageView) itemView.findViewById(R.id.btn_add_item_cart);
        btnRemove = (ImageView) itemView.findViewById(R.id.btn_remove_item_cart);
        cart_image = (ImageView) itemView.findViewById(R.id.cartImage);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}

public class MyAdapterCart extends RecyclerView.Adapter<CartViewHolder> {
    private List<Order> listData=new ArrayList<>();
    private Cart context;

    public MyAdapterCart(List<Order> listData, Cart context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Order order = listData.get(position);
        holder.txt_cart_count.setText(order.getQuantity());

//        int total_price = (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
      //  holder.txt_cart_price.setText(String.valueOf(total_price));

        holder.txt_cart_name.setText(order.getProductName());

        Picasso.with(context.getBaseContext()).load(listData.get(position).getImage()).resize(70,70).centerCrop().into(holder.cart_image);

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(order.getQuantity()) < 10){
                    order.setQuantity( String.valueOf( Integer.parseInt(order.getQuantity()) + 1 ) );
                    holder.txt_cart_count.setText(String.valueOf(order.getQuantity()));
                    int total_price = (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
                    holder.txt_cart_price.setText(String.valueOf(total_price));

                    new Database(context).updateCart(order);

                    //Upgrade total in Cart
                    int total = 0;
                    List<Order> orders = new Database(context).getCarts();
                    for (Order order : orders) {
                        total += ( (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity())) );
                    }

                   context.txtTotalPrice.setText(String.valueOf(total));
                }
            }
        });
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(order.getQuantity()) > 1){
                    order.setQuantity( String.valueOf( Integer.parseInt(order.getQuantity()) - 1 ) );
                    holder.txt_cart_count.setText(String.valueOf(order.getQuantity()));
                    int total_price = (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
                    holder.txt_cart_price.setText(String.valueOf(total_price));

                    new Database(context).updateCart(order);

                    //Upgrade total in Cart
                    int total = 0;
                    List<Order> orders = new Database(context).getCarts();
                    for (Order order : orders) {
                        total += ( (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity())) );
                    }

                    context.txtTotalPrice.setText(String.valueOf(total));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
