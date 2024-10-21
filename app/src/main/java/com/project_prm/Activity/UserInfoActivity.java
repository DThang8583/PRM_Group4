package com.example.foodappproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodappproject.databinding.ActivityRegistrationBinding;
import com.example.foodappproject.databinding.ActivityUserinfoBinding;
import com.google.firebase.auth.FirebaseAuth;

public class UserInfoActivity extends BaseActivity{
    ActivityUserinfoBinding binding;
    private String emaill;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityUserinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        emaill = intent.getStringExtra("email");
        binding.email.setText(emaill);
        setVariable();
    }

    private void setVariable() {

        binding.logoutbtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
        });
        //binding.backBtn.setOnClickListener(v -> startActivity(new Intent(UserInfoActivity.this, MainActivity.class)));
        binding.backBtn.setOnClickListener(v -> finish());
    }
}
