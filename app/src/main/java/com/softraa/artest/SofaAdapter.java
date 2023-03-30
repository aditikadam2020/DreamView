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

public class SofaAdapter extends RecyclerView.Adapter<SofaAdapter.MyViewHolder>{
    private Context context;
    private List<SofaModel> sofaModelList;

    public SofaAdapter(Context context) {
        this.context = context;
        sofaModelList=new ArrayList<>();
    }

    public void addProduct(SofaModel sofaModel){
        sofaModelList.add(sofaModel);
        notifyDataSetChanged();
    }


    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sofa_row,parent,false);
        return new SofaAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull SofaAdapter.MyViewHolder holder, int position) {
        System.out.println("On bind init");
        SofaModel sofaModel=sofaModelList.get(position);
        holder.title.setText(sofaModel.getTitle());
        holder.description.setText(sofaModel.getDescription());
        holder.price.setText(sofaModel.getPrice());
        Glide.with(context).load(sofaModel.getImage())
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SofaDetailActivity.class);
                intent.putExtra("model",sofaModel);
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return sofaModelList.size();
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
