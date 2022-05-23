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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thuchanh.TTD_19DH111156.R;
import com.thuchanh.TTD_19DH111156.RestaurantDetailActivity;
import com.thuchanh.TTD_19DH111156.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public class ViewHolderRestaurant extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvOpenHour;
        ImageView ivImage;

        public ViewHolderRestaurant(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvOpenHour = itemView.findViewById(R.id.tvOpenHour);
        }
    }

    public class ViewHolderTopRestaurant extends RecyclerView.ViewHolder {
        TextView tvName, tvRate;
        ImageView ivImage;

        public ViewHolderTopRestaurant(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.tvImage);
            tvRate = itemView.findViewById(R.id.tvRate);
        }
    }

    private ArrayList<Restaurant> mRestaurants;
    private OnRestaurantItemClickListener mListener;
    private int TYPE_LAYOUT ;

    public RestaurantAdapter(ArrayList<Restaurant> restaurants, OnRestaurantItemClickListener listener, int type_layout) {
        mRestaurants = restaurants;
        mListener = listener;
        TYPE_LAYOUT =type_layout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if(TYPE_LAYOUT == 1){
            View view = inflater.inflate(R.layout.row_restaurant, parent, false);
            return new ViewHolderRestaurant(view);
        }else {
            View view = inflater.inflate(R.layout.row_top_restaurant, parent, false);
            return new ViewHolderTopRestaurant(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Restaurant restaurant = mRestaurants.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if(TYPE_LAYOUT == 1){
            ViewHolderRestaurant viewHolderRestaurant = (ViewHolderRestaurant) viewHolder;
            //tham chiếu đến database của phần tử logo của restaurant
            StorageReference profileRef = storageReference.child("restaurants/"+ restaurant.getLogo());

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("uri", uri.toString());
                    Picasso.get().load(uri).into(viewHolderRestaurant.ivImage);
                }
            });
            viewHolderRestaurant.tvName.setText(restaurant.getName());
            viewHolderRestaurant.tvAddress.setText(restaurant.getAddress());
            viewHolderRestaurant.tvOpenHour.setText(restaurant.getOpenHours());
            viewHolderRestaurant.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRestaurantItemClick(restaurant);
                    Intent intent = new Intent(v.getContext(), RestaurantDetailActivity.class);
                    intent.putExtra("restaurant", restaurant);
                    v.getContext().startActivity(intent);
                }
            }); }
        else {
            ViewHolderTopRestaurant viewHolderTopRestaurant = (ViewHolderTopRestaurant) viewHolder;
            StorageReference profileRef = storageReference.child("restaurants/"+ restaurant.getLogo());
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(viewHolderTopRestaurant.ivImage);
                }
            });
            viewHolderTopRestaurant.tvName.setText(restaurant.getName());
            viewHolderTopRestaurant.tvRate.setText("Rate: ".concat(String.valueOf(restaurant.rate)));
            viewHolderTopRestaurant.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRestaurantItemClick(restaurant);
                    Intent intent = new Intent(v.getContext(), RestaurantDetailActivity.class);
                    intent.putExtra("restaurant", restaurant);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public void addRestaurants(ArrayList<Restaurant> restaurants){
        mRestaurants.addAll(restaurants);
        notifyDataSetChanged();
    }

    public static interface OnRestaurantItemClickListener {
        void onRestaurantItemClick(Restaurant restaurant);
    }
}
