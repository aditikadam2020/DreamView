package com.softraa.artest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private List<CartItemModel> cartModelList;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public CartAdapter(Context context) {
        this.context = context;
        cartModelList = new ArrayList<>();
    }

    public void addProduct(CartModel cartModel) {
        cartModelList.addAll(cartModel.getCartItemModelList());
        notifyDataSetChanged();
    }


    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
        return new CartAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {

        CartItemModel cartItemModel = cartModelList.get(position);
        holder.title.setText(cartItemModel.getTitle());
        holder.qtycount.setText(String.valueOf(cartItemModel.getQuantity()));
        holder.price.setText(String.valueOf(cartItemModel.getItemTotalCost()));
        Glide.with(context).load(cartItemModel.getImage())
                .into(holder.img);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance()
                        .collection("cart")
                        .whereEqualTo("userId", firebaseAuth.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                                if (dsList.isEmpty()) {
                                    Toast.makeText(v.getContext(),"Cart is already Empty",Toast.LENGTH_LONG).show();
                                } else {
                                    for (DocumentSnapshot ds : dsList) {
                                        CartModel cartModel = ds.toObject(CartModel.class);
                                        if (cartModel.getCartItemModelList().size() == 1) {
                                            FirebaseFirestore.getInstance().collection("cart").document(ds.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(v.getContext(),cartModel.getCartItemModelList().get(0).getTitle() +" removed !!",Toast.LENGTH_LONG).show();
                                                        notifyDataSetChanged();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        } else {

                                            CartItemModel modelToRemove = cartModel.getCartItemModelList().get(position);
                                            double cost = cartModel.getTotalCost() - modelToRemove.getItemTotalCost();
                                            Map<String, Object> updates = new HashMap<>();
                                            updates.put("totalCost", cost);
                                            updates.put("cartItemModelList", FieldValue.arrayRemove(modelToRemove));
                                            FirebaseFirestore.getInstance().collection("cart").document(ds.getId()).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(v.getContext(),modelToRemove.getTitle() +"removed !!",Toast.LENGTH_LONG).show();
                                                        notifyDataSetChanged();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                    }
                                }
                            }
                        });

            }
        });
    }

    public int getItemCount() {
        return cartModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, qtycount, price;
        private ImageView delete;
        private ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            qtycount = itemView.findViewById(R.id.qtycount);
            price = itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
