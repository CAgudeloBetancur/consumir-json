package com.iud.api_fetching.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iud.api_fetching.R;
import com.iud.api_fetching.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<Product> productList;
    private Context context;
    private OnItemClickListener listener;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_ui, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        /*holder.mobileName.setText(mobileList.get(position).getName());
        holder.mobilePrice.setText(mobileList.get(position).getPrice());
        holder.mobileRating.setText(mobileList.get(position).getRating());
        holder.mobileDescription.setText(mobileList.get(position).getDescription());
        Picasso.get().load(mobileList.get(position).getImg()).into(holder.mobileImg);*/
        Product product = productList.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        private TextView productName, productPrice, productRating, productDescription;
        private ImageView productImg;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name);
            productDescription = itemView.findViewById(R.id.description);
            productImg = itemView.findViewById(R.id.img);
            productPrice = itemView.findViewById(R.id.price);
            productRating = itemView.findViewById(R.id.rating);
        }

        public void bind(final Product product, final OnItemClickListener listener) {
            productName.setText(product.getName());
            productPrice.setText(product.getPrice());
            productRating.setText(product.getRating());
            productDescription.setText(product.getDescription());
            Picasso.get().load(product.getImg()).into(productImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null) listener.onItemClick(product);
                }
            });
        }
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
}
