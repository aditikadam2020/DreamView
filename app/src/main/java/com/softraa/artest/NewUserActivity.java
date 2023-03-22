package com.softraa.artest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.softraa.artest.databinding.ActivityNewUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUserActivity extends AppCompatActivity {
    String name,address,phone,email,password;
    ActivityNewUserBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth   = FirebaseAuth.getInstance();


        binding.oldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewUserActivity.this,MainActivity.class));
                finish();
            }
        });

        binding.Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String address = binding.address.getText().toString();
                String phone = binding.phone.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                createAccount(name,address,phone,email,password);
            }
        });
    }

    private void createAccount(String name,String address, String phone, String email, String password) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating");
        progressDialog.setMessage("Account");
        progressDialog.show();
        fAuth.createUserWithEmailAndPassword(email.trim(),password.trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        sendUserData(name,phone,email,address,password);
                        UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();
                        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileChangeRequest);
                        progressDialog.cancel();
                        Toast.makeText(NewUserActivity.this,"Account Created", Toast.LENGTH_SHORT);
                        binding.name.setText("");
                        binding.address.setText("");
                        binding.phone.setText("");
                        binding.email.setText("");
                        binding.password.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendUserData(String name, String email, String phone, String address, String password)                                                                                                                                                        // User data sending
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference DBref = firebaseDatabase.getReference(firebaseAuth.getUid());//uid = userid on server is used to retrieve his info only
        Users usr = new Users(name,address,phone,email,password);
        DBref.setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(NewUserActivity.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewUserActivity.this, LoginActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(NewUserActivity.this, "Registration Failed !! try again !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.getMessage());
                    }
                });
    }


}