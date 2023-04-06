package com.softraa.artest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.auth.User;
import com.google.type.DateTime;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.softraa.artest.databinding.ActivityCartBinding;
import com.softraa.artest.databinding.ActivitySofaBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CartActivity extends AppCompatActivity implements PaymentResultListener {
    ActivityCartBinding binding;

    private CartAdapter cartAdapter;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private Button checkOut;

    final String key = "rzp_test_qO9x6oybV41I3E";
    final String secret = "UEhYFfRIwfHTd66tyVWDBAIS";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference DBref = firebaseDatabase.getReference(firebaseAuth.getUid());
    OrderModel orderModel = new OrderModel();
    List<CartItemModel> orderItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartAdapter=new CartAdapter(this);

        binding.cartItemRecycler.setAdapter(cartAdapter);
        binding.cartItemRecycler.setLayoutManager(new LinearLayoutManager(this));
        checkOut = findViewById(R.id.checkOut);
        getCart();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        checkOut.setOnClickListener(v ->
        {

            FirebaseFirestore.getInstance()
                    .collection("cart")
                    .whereEqualTo("userId",firebaseAuth.getUid())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                        if (dsList.isEmpty()){
                            Toast.makeText(CartActivity.this,"Cart is empty!!!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            for (DocumentSnapshot ds:dsList){
                                //cart fetch
                                CartModel cartModel = ds.toObject(CartModel.class);
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference DBref = firebaseDatabase.getReference(firebaseAuth.getUid());
                                //address fetch
                                DBref.get().addOnSuccessListener(dataSnapshot -> {
                                    Users user = dataSnapshot.getValue(Users.class);
                                    orderModel.setAddress(user.getAddress());
                                    orderModel.setTotalCost(cartModel.getTotalCost());
                                    orderModel.setUserId(firebaseAuth.getUid());
                                    orderItems.addAll(cartModel.getCartItemModelList());
                                    orderModel.setOrderItems(orderItems);//list set ordermodel
                                    generateOrder(orderModel.getTotalCost(),user);

                                }).addOnFailureListener(e -> Toast.makeText(CartActivity.this,"Failed to fetch User data "+e.getMessage(),Toast.LENGTH_LONG).show());
                            }
                        }

                    });
        }
        );

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

    //sending request to razorpay server to generate amount order
    private void generateOrder(double requestedAmount,Users user) {

        String url = "https://api.razorpay.com/v1/orders";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String randomReceipt = UUID.randomUUID().toString();
        orderModel.setReceiptID(randomReceipt);
        //amount chi order generate karaychi request(below json)
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("amount", requestedAmount * 100);// to convert rs. to paise because razorpay requires transations in paise
            jsonBody.put("currency", "INR");
            jsonBody.put("receipt", randomReceipt);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {

                    try {
                        String orderId = response.getString("id");
                        int amount = response.getInt("amount");
                        initiatePayment(orderId, amount,randomReceipt,user);//request for payment page
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                //request headers attach (key and secret key)
                Map<String, String> headers = new HashMap<>();
                String credentials = key + ":" + secret;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);

    }


    private void initiatePayment(String orderId, int amount,String receipt,Users user) {

        Checkout checkout = new Checkout();//dependancy
        checkout.setKeyID(key);
        JSONObject object = new JSONObject();
        try {
            object.put("name", user.getName());
            object.put("description", "Reference No. "+receipt);
            object.put("image", "https://images.pexels.com/photos/11921988/pexels-photo-11921988.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"); // set your application logo URL here
            object.put("order_id", orderId);
            object.put("currency", "INR");
            object.put("amount", amount);
            object.put("prefill.email", user.getEmail());
            object.put("prefill.contact", user.getPhone());
            checkout.open(CartActivity.this, object);//page open (razorpay)

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        Date dateObj = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String timestamp = format.format(dateObj);
        orderModel.setCreatedAt(timestamp);
        orderModel.setPaymentId(s);
        FirebaseFirestore.getInstance().collection("orders").document().set(orderModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CartActivity.this, "Order placed!!!", Toast.LENGTH_LONG).show();
                FirebaseFirestore.getInstance()
                        .collection("cart")
                        .whereEqualTo("userId",firebaseAuth.getUid())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();
                            if (dsList.isEmpty()){
                                Toast.makeText(CartActivity.this,"Cart is empty!!!",Toast.LENGTH_LONG).show();
                            }
                            for (DocumentSnapshot ds:dsList){
                                ds.getReference().delete();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(CartActivity.this,"Failed to empty cart!!",Toast.LENGTH_LONG).show();
                        });

            }
        }).addOnFailureListener(e -> Toast.makeText(CartActivity.this, "Error while saving order" + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    public void onPaymentError(int i, String s)
    {
        Toast.makeText(CartActivity.this,"Error while payment: " +s, Toast.LENGTH_LONG).show();
    }



}