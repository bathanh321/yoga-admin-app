package com.example.yogaapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class OrderModel {
    @SerializedName("_id")
    private String id;

    @SerializedName("userId") // Ánh xạ đối tượng UserModel
    private UserModel user;

    @SerializedName("cartId")
    private String cartId;

    @SerializedName("total")
    private double total;

    @SerializedName("orderAt")
    private Date orderAt;

    public OrderModel() {}

    public OrderModel(String id, UserModel user, String cartId, double total, Date orderAt) {
        this.id = id;
        this.user = user;
        this.cartId = cartId;
        this.total = total;
        this.orderAt = orderAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getOrderAt(){return orderAt;}

    public void setOrderAt(Date orderAt) {this.orderAt = orderAt;}
}
