package com.softraa.artest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ElectronicsAdaptor extends RecyclerView.Adapter<ElectronicsAdaptor.MyViewHolder> {
    private Context context;
    private List<ElectronicsModel> electronicsModelList;

    public ElectronicsAdaptor(Context context) {
        this.context = context;
        electronicsModelList=new ArrayList<>();
    }

    public void addProduct(ElectronicsModel electronicsModel){
        electronicsModelList.add(electronicsModel);
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.electronics_row,parent,false);
        return new ElectronicsAdaptor.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ElectronicsAdaptor.MyViewHolder holder, int position) {
        System.out.println("On bind init");
        ElectronicsModel electronicsModel = electronicsModelList.get(position);
        holder.title.setText(electronicsModel.getTitle());
        holder.description.setText(electronicsModel.getDescription());
        holder.price.setText(electronicsModel.getPrice());
        Glide.with(context).load(electronicsModel.getImage())
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ElectronicsDetailActivity.class);
                intent.putExtra("model",electronicsModel);
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return electronicsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title,description,price;
        private ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            price=itemView.findViewById(R.id.price);
            img=itemView.findViewById(R.id.image);
        }
    }
}
