package com.softraa.artest;

import java.util.List;

public class OrderModel {
    private List<CartItemModel> orderItems;

    private  double totalCost;

    private String userId;

    private String address;

    private String createdAt;

    private String paymentId;

    private  String receiptID;


    public OrderModel(List<CartItemModel> orderItems, double totalCost, String userId, String address, String createdAt, String paymentId, String receiptID) {
        this.orderItems = orderItems;
        this.totalCost = totalCost;
        this.userId = userId;
        this.address = address;
        this.createdAt = createdAt;
        this.paymentId = paymentId;
        this.receiptID = receiptID;
    }

//    @Override
//    public String toString() {
//        return "CartModel{" +
//                "orderItems=" + orderItems +
//                ", totalCost=" + totalCost +
//                ", userId='" + userId + '\'' +
//                '}';
//    }


    @Override
    public String toString() {
        return "OrderModel{" +
                "orderItems=" + orderItems +
                ", totalCost=" + totalCost +
                ", userId='" + userId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public OrderModel() {
    }

    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<CartItemModel> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CartItemModel> orderItems) {
        this.orderItems = orderItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
