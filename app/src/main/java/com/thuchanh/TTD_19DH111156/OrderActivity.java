package com.thuchanh.TTD_19DH111156;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.thuchanh.TTD_19DH111156.adapters.FoodBasketAdapter;
import com.thuchanh.TTD_19DH111156.databases.DAOs.Cart;
import com.thuchanh.TTD_19DH111156.models.Basket;
import com.thuchanh.TTD_19DH111156.models.Food;
import com.thuchanh.TTD_19DH111156.models.FoodBasket;
import com.thuchanh.TTD_19DH111156.models.OrderFinished;
import com.thuchanh.TTD_19DH111156.models.User;
import com.thuchanh.TTD_19DH111156.repositories.CartRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OrderActivity extends FragmentActivity implements OnMapReadyCallback {
    private TextView tvTotal, tvName, tvAddress, tvMobile;
    private RecyclerView rvFoods;
    private Basket basket;
    private FoodBasketAdapter adapter;
    private Button btnPlaceOrder;
    private String restaurantKey;
    App app;
    CartRepository cartRepository;
    GoogleMap map;
    FirebaseAuth fAuth;
    FirebaseDatabase fDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        tvAddress = findViewById(R.id.tvAddress);
        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        app = App.getInstance();

        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frgMaps);
        mapFragment.getMapAsync(this);

        cartRepository = new CartRepository(getApplication());


        Intent intent = getIntent();

        if( intent.getSerializableExtra("basket") != null ){
            basket = (Basket) intent.getSerializableExtra("basket");
        }else {
            try {

                basket = new Basket();
                List<Cart> carts = cartRepository.getAllCarts();
                for (Cart cart : carts){
                    basket.addFood(new FoodBasket(new Food(cart.getFoodName(),
                            cart.getFoodImage(),
                            cart.getFoodPrice(),
                            cart.getFoodRate(),
                            cart.getResKey(),
                            cart.getFoodKey()),
                            cart.getQuantity(),
                            cart.getSum()));
                    app.setResKey(cart.getResKey());
                    Log.d("Reskey", cart.getResKey());
                }
                basket.calculateBasket();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(basket.getTotalPrice()+"");
        rvFoods = findViewById(R.id.rvFoods);
        adapter = new FoodBasketAdapter(new ArrayList<>(basket.foods.values()));
        rvFoods.setAdapter(adapter);
        rvFoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        String userID = fAuth.getCurrentUser().getUid();

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartRepository.deleteAll();

                String orderKey = fDatabase.getReference().child("orders").push().getKey();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String orderDate = sdf.format(System.currentTimeMillis());

                OrderFinished orderFinished = new OrderFinished(orderKey, orderDate, basket.getTotalPrice(), 1, userID, new ArrayList<FoodBasket>(basket.foods.values()));
                fDatabase.getReference().child("orders").child(userID).child(orderKey).setValue(orderFinished)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void aVoid) {
                                app.setOrder(orderFinished);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        String userID = fAuth.getCurrentUser().getUid();
        if (googleMap != null) {
            map = googleMap;
            fDatabase.getReference().child("users").child(userID).get()
                    .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            LatLng latLngUser = new LatLng(user.getLatitude(), user.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLngUser);
                            options.icon(BitmapDescriptorFactory.fromBitmap(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker)));

                            map.addMarker(options);
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngUser, 16));
                            tvName.setText("Name: " + user.getFirstName() + " " + user.getLastName());
                            tvAddress.setText("Address: " + user.getAddress());
                            tvMobile.setText("Mobile: " + user.getMobile());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }
}
