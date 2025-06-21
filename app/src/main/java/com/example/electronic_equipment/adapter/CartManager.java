package com.example.electronic_equipment.adapter;

import com.example.electronic_equipment.model.Cart;
import com.example.electronic_equipment.model.Product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Cart> cartItems = new ArrayList<>();

    private CartManager() {
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Add or increase quantity
    public void addToCart(Product product) {
        for (Cart item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new Cart(product, 1));
    }

    // Get all items
    public List<Cart> getCartItems() {
        return new ArrayList<>(cartItems); // prevent external modification
    }

    // Get total price
    public double getTotalPrice() {
        double total = 0;
        for (Cart item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Update quantity
    public void updateQuantity(Product product, int quantity) {
        for (Cart item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                if (quantity <= 0) {
                    removeFromCart(product);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    // Remove item
    public void removeFromCart(Product product) {
        Iterator<Cart> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            Cart item = iterator.next();
            if (item.getProduct().getId() == product.getId()) {
                iterator.remove();
                return;
            }
        }
    }

    // Clear all items
    public void clearCart() {
        cartItems.clear();
    }
}
