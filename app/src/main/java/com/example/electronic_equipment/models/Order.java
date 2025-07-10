package com.example.electronic_equipment.models;

import java.util.List;

public class Order {
    private String orderId;
    private double total;
    private String shippingAddress;
    private String status;
    private String orderDate;
    private List<OrderItem> items;


    public Order(String orderId, double total, String shippingAddress, String status, String orderDate, List<OrderItem> items) {
        this.orderId = orderId;
        this.total = total;
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.items = items;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
