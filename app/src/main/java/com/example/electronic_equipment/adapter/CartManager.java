package com.example.electronic_equipment.adapter;

import com.example.electronic_equipment.model.Cart;
import com.example.electronic_equipment.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Cart> cartItems = new ArrayList<>();

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product) {
        for (Cart item : cartItems) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new Cart(product, 1));
    }

    public List<Cart> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Cart item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void updateQuantity(Product product, int quantity) {
        for (Cart item : cartItems) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.setQuantity(quantity);
                return;
            }
        }
    }
}

