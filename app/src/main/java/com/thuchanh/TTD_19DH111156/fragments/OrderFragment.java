package com.thuchanh.TTD_19DH111156.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thuchanh.TTD_19DH111156.App;
import com.thuchanh.TTD_19DH111156.BasketDialogFragment;
import com.thuchanh.TTD_19DH111156.R;
import com.thuchanh.TTD_19DH111156.adapters.OrderAdapter;
import com.thuchanh.TTD_19DH111156.models.OrderFinished;
import com.thuchanh.TTD_19DH111156.models.Restaurant;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    App app;
    FirebaseStorage fstorage;
    TextView tvId, tvTitle, tvAddress, tvPrice, tvDate, tvTransportState;
    ImageView ivIcon;
    FirebaseDatabase fDatabase;
    ConstraintLayout basket;
    DatabaseReference dRestaurant;
    Restaurant restaurant;

    ArrayList<OrderFinished> orderFinished;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = App.getInstance();
        orderFinished = new ArrayList<>();
        fDatabase = FirebaseDatabase.getInstance();
        fstorage = FirebaseStorage.getInstance();
        tvId = view.findViewById(R.id.tvId);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvTransportState = view.findViewById(R.id.tvTransportState);
        tvDate = view.findViewById(R.id.tvDate);
        ivIcon = view.findViewById(R.id.ivIcon);
        basket = view.findViewById(R.id.basketClick);
        dRestaurant = fDatabase.getReference();

        tvId.setText(app.getOrder().getOrderID());
        tvPrice.setText(app.getOrder().getOrderSum());
        if(app.getOrder().getOrderStatus() == 1){
            tvTransportState.setText("Đang vận chuyển");
        }else{
            tvTransportState.setText("Đã vận chuyển");
        }
        tvDate.setText(app.getOrder().getOrderDate());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("restaurants/"+ app.getBasket().getFood(""));
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("uri", uri.toString());
                Picasso.get().load(uri).into(ivIcon);
            }
        });
        dRestaurant = fDatabase.getReference();
        if(app.getResKey() != null){
            Query query = dRestaurant.child("restaurants").child(app.getResKey());//query tới nơi chứa restaurant
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    restaurant = snapshot.getValue(Restaurant.class);
                    tvTitle.setText(restaurant.getName());
                    tvAddress.setText(restaurant.getAddress());
                    StorageReference profileRef = fstorage.getReference().child("restaurants/"+ restaurant.getLogo());//buov71 này phải làm dưới repository
                    profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(ivIcon);
                        }//lấy dữ liệu hình load vào
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayDialogFragment dialog = new DisplayDialogFragment(app.getBasket());
                dialog.show(getActivity().getSupportFragmentManager(), "basket_dialog");
            }
        });
    }
}