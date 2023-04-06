package com.softraa.artest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.softraa.artest.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    TextView getname,getemail,getphone,getAddress;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference DBref = firebaseDatabase.getReference(firebaseAuth.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUI();
        getUserData();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,UserMainScreenActivity.class));
                finish();
            }
        });
        binding.orders.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this,OrderActivity.class));
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "logout successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void setUI() {
        binding.getname.findViewById(R.id.getname);
        binding.getemail.findViewById(R.id.getemail);
        binding.getphone.findViewById(R.id.getphone);
        binding.getAddress.findViewById(R.id.getAddress);

    }

    private void getUserData()                                                                                                                                                        // User data sending
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference DBref = firebaseDatabase.getReference(firebaseAuth.getUid());//uid = userid on server is used to retrieve his info only
        DBref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);

                binding.getname.setText(users.getName());
                binding.getemail.setText(users.getEmail());
                binding.getphone.setText(users.getPhone());
                binding.getAddress.setText(users.getAddress());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}