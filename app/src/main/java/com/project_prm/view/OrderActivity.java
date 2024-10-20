package com.project_prm.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project_prm.view.OrderAdapter;
import com.project_prm.model.Order;
import com.project_prm.databinding.ActivityOrderBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderActivity extends BaseActivity {
    private ActivityOrderBinding binding;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        uid = getIntent().getStringExtra("uid");
        initOrder();
        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void initOrder() {
        DatabaseReference myRef = database.getReference("Order");
        ArrayList<Order> list = new ArrayList<>();
        Query query = myRef.orderByChild("Member_Id").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Order.class));
                    }
                    if(list.size() > 0){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderActivity.this);
                        binding.orderView.setLayoutManager(layoutManager);
                        OrderAdapter adapter = new OrderAdapter(list);
                        binding.orderView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}