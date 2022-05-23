package com.thuchanh.TTD_19DH111156.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.thuchanh.TTD_19DH111156.LocationServiceTask;
import com.thuchanh.TTD_19DH111156.PermissionTask;
import com.thuchanh.TTD_19DH111156.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment {
    TextInputEditText tvAddress, tvMobile;
    TextView tv_alert;
    Button btnNext;
    NavController navController;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
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
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvMobile = view.findViewById(R.id.tvMobile);
        tv_alert = view.findViewById(R.id.tv_alert_address);
        btnNext = view.findViewById(R.id.btnNext);

        tv_alert.setVisibility(View.INVISIBLE);
        btnNext.setOnClickListener(v -> {
            if (checkValid()) {

                uncheckVisibility();

                LatLng latLng = LocationServiceTask.getLatLngFromAddress(getContext(), tvAddress.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putString("address", tvAddress.getText().toString());
                bundle.putDouble("latitude", latLng.latitude);
                bundle.putDouble("longitude", latLng.longitude);
                bundle.putString("mobile", tvMobile.getText().toString());
                bundle.putString("firstname", getArguments().getString("firstname"));
                bundle.putString("lastname", getArguments().getString("lastname"));


                navController.navigate(R.id.action_addressFragment_to_usernamePasswordFragment, bundle);
            }else {
                checkVisibility();
            }
        });
    }

    private boolean checkValid() {
        if (tvAddress.getText().toString().matches("") || tvMobile.getText().toString().matches(""))
            return false;
        return true;
    }

    private void checkVisibility() {
        tv_alert.setVisibility(View.VISIBLE);
    }

    private void uncheckVisibility() {
        tv_alert.setVisibility(View.INVISIBLE);
    }

    public void getLastLocation(FragmentActivity context) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (LocationServiceTask.isLocationServiceEnabled(getActivity())) {
            if (PermissionTask.isLocationServiceAllowed(getActivity()))
                getLastLocation(getActivity());
            else
                PermissionTask.requestLocationServicePermissions(getActivity());
        } else {
            LocationServiceTask.displayEnableLocationServiceDialog(getActivity());
        }
    }
}