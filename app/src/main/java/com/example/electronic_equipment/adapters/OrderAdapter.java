package com.example.electronic_equipment.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.models.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private Context context;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderId.setText("Order #" + order.getOrderId());
        holder.txtOrderDate.setText("Order Date: " + formatDate(order.getOrderDate()));
        holder.txtShippingAddress.setText(order.getShippingAddress());

        // Format VND currency
        String formattedPrice = String.format("%,.0f VND", order.getTotal());
        holder.txtOrderTotal.setText(formattedPrice);

        // Set status với màu sắc
        holder.txtOrderStatus.setText(order.getStatus());
        setStatusColor(holder.txtOrderStatus, order.getStatus());
    }

    private void setStatusColor(TextView statusView, String status) {
        switch (status.toLowerCase()) {
            case "pending":
                statusView.setBackgroundColor(Color.parseColor("#FF9800"));
                break;
            case "confirmed":
                statusView.setBackgroundColor(Color.parseColor("#4CAF50"));
                break;
            case "shipping":
                statusView.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            case "delivered":
                statusView.setBackgroundColor(Color.parseColor("#8BC34A"));
                break;
            case "cancelled":
                statusView.setBackgroundColor(Color.parseColor("#F44336"));
                break;
            case "đã thanh toán":
                statusView.setBackgroundColor(Color.parseColor("#4CAF50"));
                statusView.setTextColor(Color.WHITE);
                break;
            default:
                statusView.setBackgroundColor(Color.parseColor("#9E9E9E"));
                break;
        }
    }

    private String formatDate(String dateString) {
        try {
            // Handle ISO 8601 format: 2025-07-22T08:38:37.880935Z
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            try {
                // Fallback to simpler format
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                Date date = inputFormat.parse(dateString);
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                return outputFormat.format(date);
            } catch (Exception ex) {
                return dateString;
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void updateOrders(List<Order> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderDate, txtShippingAddress, txtOrderTotal, txtOrderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtShippingAddress = itemView.findViewById(R.id.txtShippingAddress);
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
        }
    }
}
