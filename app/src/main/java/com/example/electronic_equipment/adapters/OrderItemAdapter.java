package com.example.electronic_equipment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.models.OrderItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<OrderItem> itemList;

    public OrderItemAdapter(List<OrderItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = itemList.get(position);
        holder.tvProductName.setText("Tên SP: " + item.getProductName());
        holder.tvQuantity.setText("SL: " + item.getQuantity());
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = vnFormat.format(item.getPrice());
        holder.tvPrice.setText("Giá: " + formattedPrice);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
//            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

}

