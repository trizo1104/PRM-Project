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
import com.example.electronic_equipment.activities.DetailActivity;
import com.example.electronic_equipment.models.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static final int VIEW_TYPE_CARD = 0;
    private static final int VIEW_TYPE_MANAGE = 1;
    private Context context;
    private List<Product> productList;
    private OnItemActionListener listener;
    private boolean isCardViewMode;

    public interface OnItemActionListener {
        default void onEdit(Product product) {
        }

        ;

        default void onDelete(Product product) {
        }

        default void onDetail(Product product) {
        }

        ;
    }

    public ProductAdapter(Context context, List<Product> productList, boolean isCardViewMode, OnItemActionListener listener) {
        this.context = context;
        this.productList = productList;
        this.isCardViewMode = isCardViewMode;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_CARD) {
            view = LayoutInflater.from(context).inflate(R.layout.item_product_card, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        }
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.txtName.setText(product.getName());
        holder.txtDescription.setText(product.getDescription());
        holder.txtQuantity.setText(String.valueOf(product.getQuantity()));

        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.txtPrice.setText(formatter.format(product.getPrice()) + " đ");

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.placeholder_image);
        }

        // Click on whole item -> open DetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("product", product); // Product should implement Serializable or Parcelable
            context.startActivity(intent);
        });

        // Edit
        if (holder.imgEdit != null) {
            holder.imgEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(product);
                }
            });
        }

        // Delete
        if (holder.imgDelete != null) {
            holder.imgDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(product);
                }
            });
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetail(product);
            }
        });
    }

    public void setCardViewMode(boolean isCardViewMode) {
        this.isCardViewMode = isCardViewMode;
        notifyDataSetChanged(); // Refresh the RecyclerView with new layout
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtDescription, txtQuantity;
        ImageView imgProduct, imgEdit, imgDelete;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtDescription = itemView.findViewById(R.id.txtProductDescription);
            txtQuantity = itemView.findViewById(R.id.txtProductQuantity);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isCardViewMode) {
            return VIEW_TYPE_CARD;
        } else {
            return VIEW_TYPE_MANAGE;
        }
    }

}
