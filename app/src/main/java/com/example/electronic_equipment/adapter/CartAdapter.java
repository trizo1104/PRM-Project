package com.example.electronic_equipment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.model.Cart;
import com.example.electronic_equipment.model.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart> cartItems;
    private OnCartChangeListener cartChangeListener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(List<Cart> cartItems, OnCartChangeListener listener) {
        this.cartItems = cartItems;
        this.cartChangeListener = listener;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price, quantity;
        ImageView btnPlus, btnMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCartProduct);
            name = itemView.findViewById(R.id.txtCartName);
            price = itemView.findViewById(R.id.txtCartPrice);
            quantity = itemView.findViewById(R.id.txtQuantity);
            btnPlus = itemView.findViewById(R.id.btnIncrease);
            btnMinus = itemView.findViewById(R.id.btnDecrease);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart item = cartItems.get(position);
        Product product = item.getProduct();

        holder.name.setText(product.getName());
        holder.price.setText("$" + String.format("%.2f", item.getTotalPrice()));
        holder.img.setImageResource(product.getImageResId());
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            notifyItemChanged(position);
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            if (currentQty > 1) {
                item.setQuantity(currentQty - 1);
                notifyItemChanged(position);
            } else {
                cartItems.remove(position);
                notifyItemRemoved(position);
            }
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
