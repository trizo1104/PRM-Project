package com.example.electronic_equipment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_equipment.R;
import com.example.electronic_equipment.models.Order;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("Mã đơn: " + order.getOrderId());
        holder.tvOrderDate.setText("Ngày: " +formatDate(order.getOrderDate()));
        holder.tvShippingAddress.setText("Địa chỉ: " + order.getShippingAddress());
        holder.tvOrderStatus.setText("Trạng thái đơn hàng: " + order.getStatus());
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = vnFormat.format(order.getTotal());
        holder.tvTotal.setText("Tổng: " + formattedPrice);

        OrderItemAdapter itemAdapter = new OrderItemAdapter(order.getItems());
        holder.recyclerOrderItems.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerOrderItems.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public String formatDate(String input) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = inputFormat.parse(input);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return input;
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvShippingAddress, tvTotal, tvOrderStatus;
        RecyclerView recyclerOrderItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvShippingAddress = itemView.findViewById(R.id.tvShippingAddress);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            recyclerOrderItems = itemView.findViewById(R.id.recyclerOrderItems);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
