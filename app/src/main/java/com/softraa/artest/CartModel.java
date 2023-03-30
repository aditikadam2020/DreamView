package com.softraa.artest;

import java.util.List;

public class CartModel {
    private List<CartItemModel> cartItemModelList;

    private  double totalCost;

    private String userId;


    public CartModel(List<CartItemModel> cartItemModelList, double totalCost, String userId) {
        this.cartItemModelList = cartItemModelList;
        this.totalCost = totalCost;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CartModel{" +
                "cartItemModelList=" + cartItemModelList +
                ", totalCost=" + totalCost +
                ", userId='" + userId + '\'' +
                '}';
    }

    public CartModel() {
    }

    public List<CartItemModel> getCartItemModelList() {
        return cartItemModelList;
    }

    public void setCartItemModelList(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
