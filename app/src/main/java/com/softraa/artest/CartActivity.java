package com.softraa.artest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.softraa.artest.databinding.ActivityCartBinding;
import com.softraa.artest.databinding.ActivitySofaBinding;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;

    private CartAdapter cartAdapter;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartAdapter=new CartAdapter(this);
        binding.cartItemRecycler.setAdapter(cartAdapter);
        binding.cartItemRecycler.setLayoutManager(new LinearLayoutManager(this));

        getCart();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getCart() {
        FirebaseFirestore.getInstance()
                .collection("cart")
                .whereEqualTo("userId",firebaseAuth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        if (dsList.isEmpty()){
                            Toast.makeText(CartActivity.this,"Cart is empty!!!",Toast.LENGTH_LONG).show();
                        }
                        for (DocumentSnapshot ds:dsList){
                            CartModel cartModel = ds.toObject(CartModel.class);

                            cartAdapter.addProduct(cartModel);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}