package com.softraa.artest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.softraa.artest.databinding.ActivityClockDetailBinding;

public class ClockDetailActivity extends AppCompatActivity {
    ActivityClockDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityClockDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClockDetailActivity.this, UserMainScreenActivity.class));
                finish();
            }
        });

        binding.trynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClockDetailActivity.this, ClockActivity.class));
                finish();
            }
        });
    }
}