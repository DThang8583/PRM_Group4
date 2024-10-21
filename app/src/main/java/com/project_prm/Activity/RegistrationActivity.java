package com.project_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.project_prm.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends BaseActivity {
    ActivityRegistrationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
    }

    private void setVariable() {
        binding.signupBtn.setOnClickListener(v -> {
            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();
            String retypePassword = binding.repassword.getText().toString();
            if (password.length() < 6){
                Toast.makeText(RegistrationActivity.this, "Your password must be 6 character", Toast.LENGTH_SHORT).show();
            }
            if (!retypePassword.equals(retypePassword)){
                Toast.makeText(RegistrationActivity.this, "Your retype password must match your password", Toast.LENGTH_SHORT).show();
            }
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, task -> {
                if (task.isSuccessful()){
                    Log.i(TAG, "onCompleted: ");
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }else {
                    Log.i(TAG, "failure: " + task.getException());
                    Toast.makeText(RegistrationActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
    public void signin(View view) {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
