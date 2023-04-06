package com.softraa.artest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.softraa.artest.databinding.ActivityElectronicsBinding;

import java.util.List;

public class ElectronicsActivity extends AppCompatActivity {

    ActivityElectronicsBinding binding;
    private ElectronicsAdaptor electronicsAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityElectronicsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        electronicsAdaptor=new ElectronicsAdaptor(this);
        binding.ElectronicsRecycler.setAdapter(electronicsAdaptor);
        binding.ElectronicsRecycler.setLayoutManager(new LinearLayoutManager(this));

        getElectronics();

        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElectronicsActivity.this,CartActivity.class));
            }
        });

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ElectronicsActivity.this, ProfileActivity.class));
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ElectronicsActivity.this, UserMainScreenActivity.class));
            }
        });
    }

    private void getElectronics() {
        FirebaseFirestore.getInstance()
                .collection("electronics")
                .whereEqualTo("show",true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList){
                            ElectronicsModel electronicsModel = ds.toObject(ElectronicsModel.class);
                            electronicsAdaptor.addProduct(electronicsModel);
                        }
                    }
                });
    }
}