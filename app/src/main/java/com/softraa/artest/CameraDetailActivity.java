package com.softraa.artest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.softraa.artest.databinding.ActivityCameraBinding;
import com.softraa.artest.databinding.ActivityCameraDetailBinding;

public class CameraDetailActivity extends AppCompatActivity {
    ActivityCameraDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCameraDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CameraDetailActivity.this, UserMainScreenActivity.class));
                finish();
            }
        });

        binding.trynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CameraDetailActivity.this, CameraActivity.class));
                finish();
            }
        });

    }
}