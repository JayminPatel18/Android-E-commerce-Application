package com.example.intershipapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    Context context;
    ArrayList<ProductList> arrayList;

    public ProductAdapter(Context homeActivity, ArrayList<ProductList> productArrayList) {
        this.context = homeActivity;
        this.arrayList = productArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name,price;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_product_image);
            name = itemView.findViewById(R.id.custom_product_name);
            price = itemView.findViewById(R.id.custom_product_price);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
       // holder.imageView.setImageResource(arrayList.get(position).getImage());
        Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.loading).into(holder.imageView);
        holder.name.setText(arrayList.get(position).getName());
        holder.price.setText(context.getResources().getString(R.string.price_symbol)+arrayList.get(position).getPrice()+" / "+arrayList.get(position).getUnit());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductDetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("name",arrayList.get(position).getName());
                bundle.putString("price",context.getResources().getString(R.string.price_symbol)+arrayList.get(position).getPrice()+"/"+arrayList.get(position).getUnit());
                bundle.putString("desc",arrayList.get(position).getDescription());
                bundle.putString("image",arrayList.get(position).getImage());

                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



}
