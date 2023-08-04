package com.example.graduatework.database;

public class Order {
    private int ID;
    private String ProductId,ProductName, Quantity, Price, Amount, Image;

    public Order(){}

    public Order(String productId, String productName, String quantity, String price, String amount,String image) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Amount = amount;
        Image = image;
    }

    public Order(int ID, String productId, String productName, String quantity, String price, String amount, String image) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Amount = amount;
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
