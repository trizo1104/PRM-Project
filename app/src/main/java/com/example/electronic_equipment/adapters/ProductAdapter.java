package com.example.electronic_equipment.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.electronic_equipment.R;
import com.example.electronic_equipment.models.Product;
//import com.example.electronic_equipment.acvitities.UpdateProductActivity;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public interface OnItemClickListener {
        void onEdit(Product product);

        void onDelete(Product product);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    // Tạo ViewHolder
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.txtQuantity.setText(String.valueOf(product.getQuantity()));
        holder.txtPrice.setText(formatter.format(product.getPrice()) + " đ");
        holder.txtDescription.setText(product.getDescription());

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.placeholder_image);
        }

        holder.imgEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(product);
            }
        });

        holder.imgDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder class
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtDescription, txtQuantity;
        ImageView imgProduct, imgEdit, imgDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtDescription = itemView.findViewById(R.id.txtProductDescription);
            txtQuantity = itemView.findViewById(R.id.txtProductQuantity);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

}
