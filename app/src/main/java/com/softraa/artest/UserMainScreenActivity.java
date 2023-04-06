package com.softraa.artest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.softraa.artest.databinding.ActivityUserMainScreenBinding;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

public class UserMainScreenActivity extends AppCompatActivity {

    ActivityUserMainScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityUserMainScreenBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        slider();

        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(UserMainScreenActivity.this,ProfileActivity.class));
            }
        });

        binding.Sofa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainScreenActivity.this, SofaActivity.class));
                finish();
                System.out.println("aditi");
            }
        });

        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(UserMainScreenActivity.this, CameraDetailActivity.class));
            }
        });

        binding.clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(UserMainScreenActivity.this, ClockDetailActivity.class));
            }
        });

        binding.laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(UserMainScreenActivity.this,LaptopDetailActivity.class));
            }
        });

        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainScreenActivity.this, CartActivity.class));
            }
        });

        binding.Electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainScreenActivity.this,ElectronicsActivity.class));
            }
        });

    }

    private void slider(){
        binding.carousel.addData(new CarouselItem("https://wear-studio.com/wp-content/uploads/2022/03/e-commerce-ar.jpeg"));
        binding.carousel.addData(new CarouselItem("https://sufio.com/content/media/images/AR-shopify-featured.width-1440.jpg"));
        binding.carousel.addData(new CarouselItem("https://arpost.co/wp-content/uploads/2019/12/shopify-AR-1024x485.png"));
        binding.carousel.addData(new CarouselItem("https://www.zakeke.com/wp-content/uploads/2021/03/dopook1.jpg"));
    }
}