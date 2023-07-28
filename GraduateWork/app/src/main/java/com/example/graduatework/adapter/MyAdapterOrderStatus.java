package com.example.graduatework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduatework.Common.ItemClickListener;
import com.example.graduatework.R;
import com.example.graduatework.database.Order;
import com.example.graduatework.database.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);
        txtOrderAddress = (TextView) itemView.findViewById(R.id.order_address);
    }

    @Override
    public void onClick(View v) {

    }
}

public class MyAdapterOrderStatus extends RecyclerView.Adapter<OrderViewHolder> {
    private Context context;
    private  List<Request> list=new ArrayList<>();

    public MyAdapterOrderStatus(Context context, List<Request> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Request request = list.get(position);
        holder.txtOrderId.setText(convertStatus(request.getName()));
        holder.txtOrderPhone.setText(request.getPhone());
        holder.txtOrderAddress.setText(request.getAddress());
        holder.txtOrderStatus.setText(request.getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String convertStatus(String status){
        if(Objects.equals(status, "0")){
            return "Заказ принят";
        }else if (Objects.equals(status, "1")){
            return "Заказ в пути";
        }else{
            return "Заказ доставлен";
        }
    }

}
