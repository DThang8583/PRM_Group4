package com.project_prm.model;

import android.content.Context;
import android.widget.Toast;

import com.project_prm.Helper.ChangeNumberItemsListener;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(Foods item, String uid) {
        ArrayList<Foods> listpop = getListCart(uid);
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if (existAlready) {
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        } else {
            listpop.add(item);
        }
        tinyDB.putListObject("CartList" + uid, listpop);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Foods> getListCart(String uid) {
        return tinyDB.getListObject("CartList" + uid);
    }

    public Double getTotalFee(String uid) {
        ArrayList<Foods> listItem = getListCart(uid);
        double fee = 0;
        for (int i = 0; i < listItem.size(); i++) {
            fee += (listItem.get(i).getPrice() * listItem.get(i).getNumberInCart());
        }
        return fee;
    }

    public void minusNumberItem(ArrayList<Foods> listItem, int position, String uid, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItem.get(position).getNumberInCart() == 1) {
            listItem.remove(position);
        } else {
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CartList" + uid, listItem); // Cập nhật giỏ hàng theo uid
        changeNumberItemsListener.change();
    }

    public void plusNumberItem(ArrayList<Foods> listItem, int position, String uid, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList" + uid, listItem); // Cập nhật giỏ hàng theo uid
        changeNumberItemsListener.change();
    }

    public void removeItem(ArrayList<Foods> listItem, int position, String uid, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.remove(position);
        tinyDB.putListObject("CartList" + uid, listItem); // Cập nhật giỏ hàng theo uid
        changeNumberItemsListener.change();
    }
}
