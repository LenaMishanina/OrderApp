package com.example.graduatework.database;

import java.util.List;

public class Request {
    private String phone, name, address, total, status, comment, payment_option;
    private List<Order> orderFoods;

    public Request() {
    }

    public Request(String phone, String name, String address, String total, String status, String comment, String payment_option, List<Order> orderFoods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;// 0 - размещен; 1 - доставляется; 2 - доставлен
        this.comment = comment;
        this.payment_option = payment_option;
        this.orderFoods = orderFoods;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPayment_option(String payment_option) {
        this.payment_option = payment_option;
    }

    public String getComment() {
        return comment;
    }

    public String getPayment_option() {
        return payment_option;
    }

    public String getStatus() {
        return status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getOrderFoods() {
        return orderFoods;
    }

    public void setOrderFoods(List<Order> orderFoods) {
        this.orderFoods = orderFoods;
    }
}
