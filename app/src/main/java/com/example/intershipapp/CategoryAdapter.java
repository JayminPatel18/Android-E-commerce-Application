package com.example.intershipapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder>{

    Context context;
    ArrayList<CategoryList>arrayList;
    SharedPreferences sp;

    public CategoryAdapter(Context homeActivity, ArrayList<CategoryList> arrayList) {

        this.context = homeActivity;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSp.PREF,Context.MODE_PRIVATE);

    }

//    public CategoryAdapter(Context homeActivity, String[] categoryNameArray, int[] categoryImageArray) {
//
//        context = homeActivity;
//        this.categoryNameArray = categoryNameArray;
//        this.categoryImageArray = categoryImageArray;
//
//    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_category_image);
            name = itemView.findViewById(R.id.custom_category_name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        // holder.imageView.setImageResource(arrayList.get(position).getImage());
        Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.loading).into(holder.imageView);
        holder.name.setText(arrayList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.CATEGORY_ID,arrayList.get(position).getId()).commit();
                new CommonMethod(context,ProductActivity.class);
            }
        });

    }

    @Override
    public int getItemCount() { return arrayList.size();}

}
