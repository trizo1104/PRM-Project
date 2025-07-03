package com.example.electronic_equipment.adapters;

import com.example.electronic_equipment.models.Cart;
import com.example.electronic_equipment.models.Product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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

    // Thêm sản phẩm vào giỏ hoặc tăng số lượng nếu đã có
    public void addToCart(Product product) {
        if (product == null || product.getProductId() == null) return;

        for (Cart item : cartItems) {
            if (item.getProductId().equals(product.getProductId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        // Nếu chưa tồn tại, tạo mới Cart
        Cart newItem = new Cart(
                UUID.randomUUID().toString(),           // cartItemId
                product.getProductId(),                 // productId
                product.getName(),               // productName
                product.getPrice(),                     // price
                1,                                      // quantity
                java.time.ZonedDateTime.now().toString(),// addedAt
                product.getImageUrl()
        );
        cartItems.add(newItem);
    }

    // Lấy danh sách sản phẩm trong giỏ
    public List<Cart> getCartItems() {
        return new ArrayList<>(cartItems); // trả về bản sao để tránh sửa trực tiếp
    }

    // Tính tổng giá
    public double getTotalPrice() {
        double total = 0;
        for (Cart item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Cập nhật số lượng sản phẩm
    public void updateQuantity(String productId, int quantity) {
        if (productId == null) return;

        for (Cart item : cartItems) {
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    removeFromCart(productId);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    // Xoá sản phẩm khỏi giỏ
    public void removeFromCart(String productId) {
        if (productId == null) return;

        Iterator<Cart> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            Cart item = iterator.next();
            if (item.getProductId().equals(productId)) {
                iterator.remove();
                return;
            }
        }
    }

    // Xoá toàn bộ giỏ hàng
    public void clearCart() {
        cartItems.clear();
    }
}
