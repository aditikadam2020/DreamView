package com.softraa.artest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.softraa.artest.databinding.ActivityDashBoardBinding;
import com.softraa.artest.databinding.ActivityDetailBinding;

import java.util.UUID;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private ProductModel productModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        productModel = (ProductModel) intent.getSerializableExtra("model");
        binding.title.setText(productModel.getTitle());
        binding.description.setText(productModel.getDescription());
        binding.price.setText(productModel.getPrice());

        Glide.with(binding.getRoot())
                .load(productModel.getImage())
                .into(binding.ProductImage);

        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog= new BottomSheetDialog(this);
        View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.bottom_layout,(LinearLayout)findViewById(R.id.mainLayout),false);
        bottomSheetDialog.setContentView(view);
        EditText qty=view.findViewById(R.id.qty);
        Button btn=view.findViewById(R.id.conti);
        bottomSheetDialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quanti = qty.getText().toString();
                addtocart(quanti);
                bottomSheetDialog.cancel();
            }
        });
    }

    private void addtocart(String qty) {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Adding");
        progressDialog.setMessage("Item In Cart");
        progressDialog.show();
        String id = UUID.randomUUID().toString();
//        CartModel cartModel = new CartModel(id,productModel.getTitle(),productModel.getImage(),productModel.getPrice(),qty, FirebaseAuth.getInstance().getUid());
//
//        FirebaseFirestore.getInstance()
//                .collection("cart")
//                .document(id)
//                .set(cartModel);
//        Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show();
//        progressDialog.cancel();
//    }
}}