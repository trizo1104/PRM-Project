package com.example.electronic_equipment.adapters;

import android.content.Context;
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

    // Bind dữ liệu vào ViewHolder
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


//        holder.imgEdit.setOnClickListener(v -> {
//            Intent intent = new Intent(context, UpdateProductActivity.class);
//            intent.putExtra("product", product);
//            context.startActivity(intent);
//        });

//        holder.imgDelete.setOnClickListener(v -> {
//            if (context instanceof ProductListActivity) {
//                ((ProductListActivity) context).deleteProduct(product.getProductId());
//            }
//        });
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
