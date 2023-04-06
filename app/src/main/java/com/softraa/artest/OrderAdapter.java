package com.softraa.artest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private Context context;
    private List<CartItemModel> orderItemsList;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public OrderAdapter(Context context) {
        this.context = context;
        orderItemsList = new ArrayList<>();
    }

    public void addProduct(OrderModel orderModel) {
        orderItemsList.addAll(orderModel.getOrderItems());

        System.out.println("Order Items received in adapter" + orderItemsList.size());
        notifyDataSetChanged();
    }


    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false);
        return new OrderAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, int position) {

        CartItemModel cartItemModel = orderItemsList.get(position);
        holder.title.setText(cartItemModel.getTitle());
        holder.qtycount.setText(String.valueOf(cartItemModel.getQuantity()));
        holder.price.setText(String.valueOf(cartItemModel.getPrice()));
        holder.TotalCost.setText(String.valueOf(cartItemModel.getItemTotalCost()));
        Glide.with(context).load(cartItemModel.getImage())
                .into(holder.img);


    }

    public int getItemCount() {
        return orderItemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, qtycount, price,TotalCost;

        private ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            qtycount = itemView.findViewById(R.id.qtycount);
            price = itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.image);
            TotalCost = itemView.findViewById(R.id.TotalCost);
        }
    }
}
