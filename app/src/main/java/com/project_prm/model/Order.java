package com.project_prm.model;

public class Order {
    private String Id;
    private String Member_Id;
    private float Total_Price;

    public Order() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(String member_Id) {
        Member_Id = member_Id;
    }

    public float getTotal_Price() {
        return Total_Price;
    }

    public void setTotal_Price(float total_Price) {
        Total_Price = total_Price;
    }
}
