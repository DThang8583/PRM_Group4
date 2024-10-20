package com.project_prm.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project_prm.Api.CreateOrder;
import com.project_prm.view.CartAdapter;
import com.project_prm.Api.CreateOrder;
import com.project_prm.Helper.ChangeNumberItemsListener;
import com.project_prm.model.ManagementCart;
import com.project_prm.model.TinyDB;
import com.project_prm.databinding.ActivityCartBinding;
import com.project_prm.Helper.ChangeNumberItemsListener;
import com.project_prm.databinding.ActivityCartBinding;
import com.project_prm.model.ManagementCart;
import com.project_prm.model.TinyDB;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    private ManagementCart managementCart;
    private String uid;
    private double tax;
    private TinyDB tinyDB;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Khởi tạo ZaloPay SDK
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        managementCart = new ManagementCart(this);
        uid = getIntent().getStringExtra("uid");
        calculateCard();
        initCartList();
        setVariable();
        binding.btnThanhToan.setOnClickListener(v -> handlePayment());
    }

    private void handlePayment() {
        CreateOrder orderApi = new CreateOrder();
        try {
            // Truyền totalAmount đã tính toán vào createOrder
            double percentTax = 0.02; // 2% thuế
            double delivery = 10; // 10 đô la

            // Tính toán thuế và tổng số tiền
            tax = Math.round(managementCart.getTotalFee(uid) * percentTax * 100.0) / 100;
            double total = Math.round((managementCart.getTotalFee(uid) + tax + delivery) * 100) / 100;
            String totalString = String.valueOf((int) total);
            JSONObject data = orderApi.createOrder(totalString);
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                // Bắt đầu thanh toán bằng ZaloPay SDK
                ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String transactionId, String message, String data) {
                        TinyDB tinyDB = new TinyDB(CartActivity.this); // Assuming you're in an Activity context
                        tinyDB.clear(); // Xóa giỏ hàng
                        Toast.makeText(CartActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

                        // Cập nhật lại giỏ hàng
                        initCartList(); // Gọi lại phương thức để khởi tạo danh sách giỏ hàng
                    }

                    @Override
                    public void onPaymentCanceled(String transactionId, String message) {
                        Toast.makeText(CartActivity.this, "Thanh toán bị hủy!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String transactionId, String message) {
                        Toast.makeText(CartActivity.this, "Có lỗi xảy ra: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CartActivity.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initCartList() {
        // Kiểm tra xem giỏ hàng có rỗng không
        if (managementCart.getListCart(uid).isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.backToShoppingBtn.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.backToShoppingBtn.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        // Thiết lập LayoutManager cho RecyclerView
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Khởi tạo CartAdapter với đủ các tham số
        binding.cartView.setAdapter(new CartAdapter(managementCart.getListCart(uid), managementCart, new ChangeNumberItemsListener() {
            public void change() {
                calculateCard(); // Gọi lại calculateCard khi có sự thay đổi số lượng
                initCartList();
            }
        }, uid));
    }

    private void calculateCard() {
        double percentTax = 0.02; // 2% thuế
        double delivery = 10000; // 10 đô la phí giao hàng
        tax = Math.round(managementCart.getTotalFee(uid) * percentTax * 100.0) / 100;
        double total = Math.round((managementCart.getTotalFee(uid) + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managementCart.getTotalFee(uid) * 100) / 100;

        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        binding.totalFeeTxt.setText(decimalFormat.format(itemTotal) + " VNĐ");
        binding.taxTxt.setText(decimalFormat.format(tax) + " VNĐ");
        binding.deliveryTxt.setText(decimalFormat.format(delivery) + " VNĐ");
        binding.totalTxt.setText(decimalFormat.format(total) + " VNĐ");
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.backToShoppingBtn.setOnClickListener(v -> finish());
        binding.checkOutBtn.setOnClickListener(v -> {
            Map<String, Object> orderData = new HashMap<>();
            double total = Math.round((managementCart.getTotalFee(uid) + tax + 10) * 100) / 100;
            String orderId = "OrderId"; // Tạo ID đơn hàng
            orderData.put("Member_Id", uid);
            orderData.put("Total_Price", total);
            // Lưu đơn hàng
            // clearCart();
            Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
