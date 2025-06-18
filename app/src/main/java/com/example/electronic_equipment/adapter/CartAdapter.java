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

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cart> cartItems;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private OnCartChangeListener cartChangeListener;

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

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Cart item = cartItems.get(position);
        holder.name.setText(item.getProduct().getName());
        holder.price.setText("$" + item.getTotalPrice());
        holder.img.setImageResource(item.getProduct().getImageResId());
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                if (cartChangeListener != null) cartChangeListener.onCartChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}

