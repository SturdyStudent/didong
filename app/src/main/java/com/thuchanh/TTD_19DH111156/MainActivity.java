package com.thuchanh.TTD_19DH111156;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thuchanh.TTD_19DH111156.models.User;
import com.thuchanh.TTD_19DH111156.repositories.CartRepository;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    TextView textCartItemCount;
    int mCartItemCount = 0;
    App app;
    Menu mMenu;
    boolean flag = true;
    CartRepository cartRepository;
    FirebaseDatabase fDatabase;
    FirebaseAuth fAuth;

    TextView tvFullName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fDatabase = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();


        app = (App) getApplication();
        cartRepository = new CartRepository(getApplication());


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout_fragment:
                        Toast.makeText(getApplicationContext(), "touched", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        getMenuInflater().inflate(R.menu.cart_menu, toolbar.getMenu());
        mMenu = toolbar.getMenu();

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                drawer,
                toolbar,
                R.string.open,
                R.string.close);

        drawer.addDrawerListener(actionBarDrawerToggle);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.orderFragment, R.id.profileFragment, R.id.logout_fragment)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.profileFragment) {
                    mMenu.findItem(R.id.mnuCart).setVisible(false);
                } else if (mMenu != null) {
                    mMenu.findItem(R.id.mnuCart).setVisible(true);
                }
            }
        });

        View view = navigationView.getHeaderView(0);
        tvFullName = view.findViewById(R.id.tvFullname);
        tvEmail = view.findViewById(R.id.tvEmail);

        String userID = fAuth.getCurrentUser().getUid();
        fDatabase.getReference().child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                user.setId(userID);
                tvFullName.setText(user.getFirstName() + " " + user.getLastName());
                tvEmail.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fDatabase.getReference().child("users").child(userID).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setId(userID);
                        tvFullName.setText(user.getFirstName() + " " + user.getLastName());
                        tvEmail.setText(user.getEmail());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mCartItemCount = cartRepository.getCountCart();
            setupBadge();
            Log.d("ABC", mCartItemCount+"");
        } catch (ExecutionException e) {
            Log.d("ABC", e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("ABC", e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cart_menu, menu);
        mMenu = menu;


        View actionView = mMenu.findItem(R.id.mnuCart).getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge); // set view của giỏ hàng

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(mMenu.findItem(R.id.mnuCart));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnuCart) {
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupBadge() { //xét nếu giỏ hàng có hàng thì hiện số, không thì thôi

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99))); //trả về số nhỏ nhất của các tham số
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}
