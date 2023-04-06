package com.softraa.artest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.softraa.artest.databinding.ActivityCartBinding;
import com.softraa.artest.databinding.ActivityOrderBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    ActivityOrderBinding binding;

    private OrderAdapter orderAdapter;



    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderAdapter =new OrderAdapter(this);

        binding.orderItemRecycler.setAdapter(orderAdapter);
        binding.orderItemRecycler.setLayoutManager(new LinearLayoutManager(this));
        getOrders();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getOrders() {
        FirebaseFirestore.getInstance()
                .collection("orders")
                .whereEqualTo("userId",firebaseAuth.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                    if (dsList.isEmpty())
                    {
                        Toast.makeText(OrderActivity.this,"No orders available",Toast.LENGTH_LONG).show();
                    }
                    for (DocumentSnapshot ds:dsList){
                        OrderModel orderModel = ds.toObject(OrderModel.class);
                        orderAdapter.addProduct(orderModel);
                    }
                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}