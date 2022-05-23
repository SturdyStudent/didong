package com.thuchanh.TTD_19DH111156.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thuchanh.TTD_19DH111156.LocationServiceTask;
import com.thuchanh.TTD_19DH111156.R;
import com.thuchanh.TTD_19DH111156.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    TextView tvAlert, tvFullName, tvEmail;
    AppCompatButton btnUpdate, btnChangePass;
    EditText edtUsername, edtEmail, edtPhone, edtAddress;
    FirebaseAuth fAuth;
    FirebaseDatabase fDatabase;
    DatabaseReference databaseReference;
    User currentUser;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtPhone = view.findViewById(R.id.edtPhone);
        tvAlert = view.findViewById(R.id.tv_alert_profile);
        tvAlert.setVisibility(View.INVISIBLE);

        fDatabase = FirebaseDatabase.getInstance();
        databaseReference = fDatabase.getReference("users");
        fAuth = FirebaseAuth.getInstance();

        getCurrentUser();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValid()) {
                    updateUser();
                    tvAlert.setVisibility(View.INVISIBLE);
                }else{
                    tvAlert.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void getCurrentUser(){
        databaseReference.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUser = snapshot.getValue(User.class);
                    edtAddress.setText(currentUser.getAddress());
                    edtEmail.setText(currentUser.getEmail());
                    edtUsername.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
                    edtPhone.setText(currentUser.getMobile());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void updateUser(){
        String[] hoten = edtUsername.getText().toString().split(" ",2);
        String ho = hoten[0];
        String ten = hoten[1];


        LatLng latLng = LocationServiceTask.getLatLngFromAddress(getContext(), edtAddress.getText().toString());
        Map<String, Object> user = new HashMap<>();

        user.put("address", edtAddress.getText().toString());
        user.put("email", edtEmail.getText().toString());
        user.put("mobile", edtPhone.getText().toString());
        user.put("firstname", ho);
        user.put("lastname", ten);
        user.put("latitude", latLng.latitude);
        user.put("longitude", latLng.longitude);

        fDatabase.getReference().child("users").child(fAuth.getCurrentUser().getUid()).updateChildren(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private boolean checkValid() {
        if (!edtPhone.getText().toString().matches("") &&
                !edtUsername.getText().toString().matches("") &&
                !edtEmail.getText().toString().matches("") &&
                !edtAddress.getText().toString().matches("")) {
            return true;
        }
        return false;
    }
}