package com.thuchanh.TTD_19DH111156.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.thuchanh.TTD_19DH111156.OrderActivity;
import com.thuchanh.TTD_19DH111156.R;
import com.thuchanh.TTD_19DH111156.RestaurantDetailActivity;
import com.thuchanh.TTD_19DH111156.adapters.FoodBasketAdapter;
import com.thuchanh.TTD_19DH111156.models.Basket;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DisplayDialogFragment extends DialogFragment{
    public TextView tvTotal;
    public RecyclerView rvFoods;
    public Basket basket;
    public FoodBasketAdapter adapter;
    public Button btnPlaceOrder;



    @SuppressLint("ValidFragment")
    public DisplayDialogFragment(Basket basket) {
        this.basket = basket;
    }

    public DisplayDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvTotal.setText(basket.getTotalPrice()+"");
        rvFoods = view.findViewById(R.id.rvFoods);
        adapter = new FoodBasketAdapter(new ArrayList<>(basket.foods.values()));
        rvFoods.setAdapter(adapter);
        rvFoods.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCancelable(true);
        super.onResume();
    }
}