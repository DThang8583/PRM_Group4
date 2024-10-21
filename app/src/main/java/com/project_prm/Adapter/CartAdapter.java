package com.project_prm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.project_prm.Domain.Foods;
import com.project_prm.Helper.ChangeNumberItemsListener;
import com.project_prm.R;
import com.project_prm.Helper.ManagementCart;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<Foods> list;
    private ManagementCart managementCart;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private String uid; // Thêm biến uid

    public CartAdapter(ArrayList<Foods> list, ManagementCart managementCart, ChangeNumberItemsListener changeNumberItemsListener, String uid) {
        this.list = list;
        this.managementCart = managementCart;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.uid = uid; // Gán uid
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods foodItem = list.get(position);
        holder.title.setText(foodItem.getTitle());

        // Cập nhật giá trị mỗi lần gọi
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedFeeEachItem = decimalFormat.format(foodItem.getNumberInCart() * foodItem.getPrice());
        holder.feeEachItem.setText(formattedFeeEachItem + " VNĐ");
        holder.num.setText(String.valueOf(foodItem.getNumberInCart()));

        Glide.with(holder.itemView.getContext())
                .load(foodItem.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(v -> {
            managementCart.plusNumberItem(list, position, uid, () -> {
                notifyItemChanged(position); // Cập nhật chỉ vị trí đã thay đổi
                changeNumberItemsListener.change();
            });
        });

        holder.minusItem.setOnClickListener(v -> {
            managementCart.minusNumberItem(list, position, uid, () -> {
                notifyItemChanged(position); // Cập nhật chỉ vị trí đã thay đổi
                changeNumberItemsListener.change();
            });
        });

        holder.trashBtn.setOnClickListener(v -> {
            managementCart.removeItem(list, position, uid, () -> {
                notifyItemRemoved(position); // Cập nhật vị trí đã bị xóa
                changeNumberItemsListener.change();
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem;
        ImageView pic;
        TextView num;
        ConstraintLayout trashBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            num = itemView.findViewById(R.id.numberItemTxt);
            trashBtn = itemView.findViewById(R.id.trashBtn);
        }
    }
}
