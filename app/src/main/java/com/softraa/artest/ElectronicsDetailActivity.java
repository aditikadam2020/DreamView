package com.softraa.artest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.softraa.artest.databinding.ActivityElectronicsDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class ElectronicsDetailActivity extends AppCompatActivity {
    ActivityElectronicsDetailBinding binding;
    private ElectronicsModel electronicsModel;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityElectronicsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        electronicsModel = (ElectronicsModel) intent.getSerializableExtra("model");
        binding.title.setText(electronicsModel.getTitle());
        binding.description.setText(electronicsModel.getDescription());
        binding.price.setText(electronicsModel.getPrice());

        Glide.with(binding.getRoot())
                .load(electronicsModel.getImage())
                .into(binding.ElectronicsImage);

        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });

        binding.trynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(binding.getRoot().getContext(), ElectronicsArActivity.class);
                intent.putExtra("model", electronicsModel);
                binding.getRoot().getContext().startActivity(intent);
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ElectronicsDetailActivity.this, ElectronicsActivity.class));
            }
        });

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElectronicsDetailActivity.this, ProfileActivity.class));
            }
        });

        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElectronicsDetailActivity.this,CartActivity.class));
            }
        });
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(ElectronicsDetailActivity.this).inflate(R.layout.bottom_layout, (LinearLayout) findViewById(R.id.mainLayout), false);
        bottomSheetDialog.setContentView(view);
        EditText qty = view.findViewById(R.id.qty);
        Button btn = view.findViewById(R.id.conti);
        bottomSheetDialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItemModel cartItemModel = new CartItemModel();
                cartItemModel.setQuantity(Integer.parseInt(qty.getText().toString()));
                cartItemModel.setImage(electronicsModel.getImage());
                cartItemModel.setTitle(electronicsModel.getTitle());
                cartItemModel.setPrice(Double.parseDouble(electronicsModel.getPrice().replace("₹", "")));
                cartItemModel.setItemTotalCost(cartItemModel.getPrice() * cartItemModel.getQuantity());
                FirebaseFirestore.getInstance()
                        .collection("cart")
                        .whereEqualTo("userId", firebaseAuth.getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                                if (dsList.isEmpty()) {
                                    CartModel model = new CartModel();
                                    List<CartItemModel> list = new ArrayList<>();
                                    list.add(cartItemModel);
                                    model.setCartItemModelList(list);
                                    model.setUserId(firebaseAuth.getUid());
                                    model.setTotalCost(cartItemModel.getPrice() * cartItemModel.getQuantity());
                                    FirebaseFirestore.getInstance().collection("cart").document().set(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(v.getContext(), cartItemModel.getTitle() + " added!!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ElectronicsDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    for (DocumentSnapshot ds : dsList) {
                                        CartModel cartModel = ds.toObject(CartModel.class);
                                        cartModel.getCartItemModelList().add(cartItemModel);
                                        cartModel.setTotalCost(cartModel.getTotalCost() + (cartItemModel.getPrice() * cartItemModel.getQuantity()));
                                        FirebaseFirestore.getInstance().collection("cart").document(ds.getId()).set(cartModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(v.getContext(), cartItemModel.getTitle() + " added!!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ElectronicsDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                }


                            }
                        });
                bottomSheetDialog.cancel();
            }
        });
    }
}