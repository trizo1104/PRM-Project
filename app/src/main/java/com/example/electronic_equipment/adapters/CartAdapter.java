package com.example.electronic_equipment.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.electronic_equipment.Fragment.CartFragment;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.activities.DetailActivity;
import com.example.electronic_equipment.models.Cart;
import com.example.electronic_equipment.models.CartResponse;
import com.example.electronic_equipment.models.Product;
import com.example.electronic_equipment.networks.CartApi;
import com.example.electronic_equipment.networks.RetrofitClient;
import com.example.electronic_equipment.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
        private Context context;
    private List<Cart> cartItems;
    private OnCartChangeListener cartChangeListener;

    private CartApi cartApi;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<Cart> cartItems, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartChangeListener = listener;
        this.cartApi = RetrofitClient.getInstance().create(CartApi.class); // ✅ INIT
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

        holder.name.setText(item.getProductName());
        holder.price.setText("$" + String.format("%.2f", item.getTotalPrice()));
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .into(holder.img);

        holder.quantity.setText(String.valueOf(item.getQuantity()));
        SessionManager sessionManager = new SessionManager(context);
        String userId = sessionManager.getUserId();
        Log.d("lsjlkajd", userId);

        holder.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            notifyItemChanged(position);
            if (cartChangeListener != null) cartChangeListener.onCartChanged();
            cartApi.addToCart(item.getProductId(), userId, 1).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("API", "Fetched " + response + " products");
                    if (response.isSuccessful()) {
                        Toast.makeText(holder.itemView.getContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(holder.itemView.getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            Cart currentItem = cartItems.get(currentPosition);

            if (currentItem.getQuantity() > 1) {
                // Decrease quantity locally
                int newQty = currentItem.getQuantity() - 1;
                currentItem.setQuantity(newQty);
                notifyItemChanged(currentPosition);

                // Call API to decrease quantity
                cartApi.decreaseCart(item.getProductId(), userId, 1)
                        .enqueue(new Callback<CartResponse>() {
                            @Override
                            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                                Log.d("asdasd", String.valueOf(response));
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Đã giảm số lượng trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                                    if (cartChangeListener != null) {
                                        cartChangeListener.onCartChanged();
                                    }
                                } else {
                                    Toast.makeText(context, "Giảm số lượng thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CartResponse> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {
                // Quantity == 1 → remove item entirely
                cartItems.remove(currentPosition);
                notifyItemRemoved(currentPosition);

                cartApi.decreaseCart(currentItem.getProductId(), userId, 1)
                        .enqueue(new Callback<CartResponse>() {
                            @Override
                            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng!", Toast.LENGTH_SHORT).show();
                                    if (cartChangeListener != null) {
                                        cartChangeListener.onCartChanged();
                                    }
                                } else {
                                    Toast.makeText(context, "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CartResponse> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
