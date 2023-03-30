package com.softraa.artest;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.softraa.artest.databinding.ActivitySofaBinding;

import java.util.List;

public class SofaActivity extends AppCompatActivity {
    ActivitySofaBinding binding;
    private SofaAdapter sofaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySofaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sofaAdapter=new SofaAdapter(this);
        binding.sofaRecycler.setAdapter(sofaAdapter);
        binding.sofaRecycler.setLayoutManager(new LinearLayoutManager(this));

        getSofa();

        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(DashBoardActivity.this,CartActivity.class));
            }
        });
    }

    private void getSofa() {
        FirebaseFirestore.getInstance()
                .collection("sofa")
                .whereEqualTo("show",true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot ds:dsList){
                            SofaModel sofaModel = ds.toObject(SofaModel.class);
                            sofaAdapter.addProduct(sofaModel);
                        }
                    }
                });
    }

}