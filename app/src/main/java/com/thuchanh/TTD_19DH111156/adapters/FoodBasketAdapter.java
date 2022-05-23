package com.thuchanh.TTD_19DH111156.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thuchanh.TTD_19DH111156.AddToBasketDialogFragment;
import com.thuchanh.TTD_19DH111156.R;
import com.thuchanh.TTD_19DH111156.models.Food;
import com.thuchanh.TTD_19DH111156.models.FoodBasket;

import java.util.ArrayList;

public class FoodBasketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FoodBasket> foodBasketList;
    public FoodBasketAdapter(ArrayList<FoodBasket> foodBasketList){
        this.foodBasketList = foodBasketList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_food_basket, parent, false);
        return new FoodBasketAdapter.ViewHolderFoodBasket(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FoodBasket foodBasket = foodBasketList.get(position);
        FoodBasketAdapter.ViewHolderFoodBasket viewHolderFoodBasket = (FoodBasketAdapter.ViewHolderFoodBasket) holder;

        viewHolderFoodBasket.tvPrice.setText(foodBasket.getPrice() + "VNĐ");
        viewHolderFoodBasket.tvName.setText(foodBasket.getName());
        viewHolderFoodBasket.tvTotal.setText(String.valueOf(foodBasket.getPrice() * foodBasket.getQuantity()));
        viewHolderFoodBasket.tvQuantity.setText(String.valueOf(foodBasket.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return foodBasketList.size();
    }
    public class ViewHolderFoodBasket extends RecyclerView.ViewHolder{
        TextView tvName, tvTotal, tvPrice, tvQuantity;
        public ViewHolderFoodBasket(@NonNull View itemView) {
            super(itemView);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvName = itemView.findViewById(R.id.tvName);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
