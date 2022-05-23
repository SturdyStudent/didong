package com.thuchanh.TTD_19DH111156.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thuchanh.TTD_19DH111156.R;
import com.thuchanh.TTD_19DH111156.RestaurantDetailActivity;
import com.thuchanh.TTD_19DH111156.models.Food;
import com.thuchanh.TTD_19DH111156.models.Restaurant;

import java.util.ArrayList;

public class TopFoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<Food> foods;
    OnTopFoodItemClickListener mListener;

    public TopFoodAdapter(ArrayList<Food> foods, OnTopFoodItemClickListener listener){
        this.foods = foods;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_top_food, parent, false);
        return new ViewHolderTopFood(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Food food = foods.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("restaurants/"+ food.getImage());
        ViewHolderTopFood viewHolderTopFood = (ViewHolderTopFood) holder;
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("uri", uri.toString());
                Picasso.get().load(uri).into(viewHolderTopFood.imvFood);
            }
        });
        viewHolderTopFood.tvPrice.setText(String.valueOf(food.getPrice()));
        viewHolderTopFood.tvRate.setText("Rate: ".concat(String.valueOf(food.getRate())));
        viewHolderTopFood.tvName.setText(food.getName());
        viewHolderTopFood.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTopFoodItemClick(food);
                Intent intent = new Intent(view.getContext(), RestaurantDetailActivity.class);
                intent.putExtra("food", food);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
    public class ViewHolderTopFood extends RecyclerView.ViewHolder{
        ImageView imvFood;
        TextView tvName, tvRate, tvPrice;
        public ViewHolderTopFood(@NonNull View itemView) {
            super(itemView);
            imvFood = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
    public static interface OnTopFoodItemClickListener {
        void onTopFoodItemClick(Food food);
    }
}
