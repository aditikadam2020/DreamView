package com.softraa.artest;

public class CartItemModel {

    private String title;
    private String image;
    private  double price;

    private int quantity;

    private double itemTotalCost;

    public CartItemModel(String title, String image, double price, int quantity, double itemTotalCost) {
        this.title = title;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.itemTotalCost = itemTotalCost;
    }

    @Override
    public String toString() {
        return "CartItemModel{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", itemTotalCost=" + itemTotalCost +
                '}';
    }

    public CartItemModel() {
    }

    public String getTitle() {
        return title;
    }

    public double getItemTotalCost() {
        return itemTotalCost;
    }

    public void setItemTotalCost(double itemTotalCost) {
        this.itemTotalCost = itemTotalCost;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
