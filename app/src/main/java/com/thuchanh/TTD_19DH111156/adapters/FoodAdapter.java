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
import com.thuchanh.TTD_19DH111156.App;
import com.thuchanh.TTD_19DH111156.BasketDialogFragment;
import com.thuchanh.TTD_19DH111156.R;
import com.thuchanh.TTD_19DH111156.models.Food;
import com.thuchanh.TTD_19DH111156.models.FoodBasket;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Food> foods;
    Context mContext;
    App app;

    public FoodAdapter(ArrayList<Food> foods, Context mContext) {
        this.foods = foods;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_top_food, parent, false);
        return new ViewHolderFood(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Food food = foods.get(position);
        app = App.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("restaurants/"+ food.getImage());
        FoodAdapter.ViewHolderFood viewHolderFood = (FoodAdapter.ViewHolderFood) holder;
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("uri", uri.toString());
                Picasso.get().load(uri).into(viewHolderFood.imvFood);
            }
        });
        viewHolderFood.tvPrice.setText(String.valueOf(food.getPrice()));
        viewHolderFood.tvRate.setText("Rate: ".concat(String.valueOf(food.getRate())));
        viewHolderFood.tvName.setText(food.getName());
        FragmentActivity activity = (FragmentActivity)(mContext);
        FragmentManager fm = activity.getSupportFragmentManager();
        app.setResKey(food.getResKey());
        AddToBasketDialogFragment addToBasket = new AddToBasketDialogFragment(new FoodBasket(food, 0,0));
        viewHolderFood.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToBasket.show(fm, "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
    public class ViewHolderFood extends RecyclerView.ViewHolder{
        ImageView imvFood;
        TextView tvName, tvRate, tvPrice;
        public ViewHolderFood(@NonNull View itemView) {
            super(itemView);
            imvFood = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
    public static interface OnFoodItemClickListener {
        void onFoodItemClick(Food food);
    }
}
