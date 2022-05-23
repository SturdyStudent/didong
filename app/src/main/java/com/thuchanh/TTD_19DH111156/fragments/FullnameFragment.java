package com.thuchanh.TTD_19DH111156.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.thuchanh.TTD_19DH111156.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullnameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullnameFragment extends Fragment {
    TextInputEditText tvFirstName, tvLastName;
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

    public FullnameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullnameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FullnameFragment newInstance(String param1, String param2) {
        FullnameFragment fragment = new FullnameFragment();
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
        return inflater.inflate(R.layout.fragment_fullname, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        tvFirstName = view.findViewById(R.id.tvFirstName);
        tvLastName = view.findViewById(R.id.tvLastName);
        tv_alert = view.findViewById(R.id.tv_alert_fullname);
        btnNext = view.findViewById(R.id.btnNext);

        tv_alert.setVisibility(View.INVISIBLE);
        btnNext.setOnClickListener(v -> {
            if (checkValid()) {

                uncheckVisibility();
                Bundle bundle = new Bundle();
                bundle.putString("firstname", tvFirstName.getText().toString());
                bundle.putString("lastname", tvLastName.getText().toString());
                navController.navigate(R.id.action_fullnameFragment_to_addressFragment, bundle);
            } else {
                checkVisibility();
            }
        });
    }

    private boolean checkValid() {
        if (tvFirstName.getText().toString().matches("") || tvLastName.getText().toString().matches(""))
            return false;
        return true;
    }

    private void checkVisibility() {
        tv_alert.setVisibility(View.VISIBLE);
    }

    private void uncheckVisibility() {
        tv_alert.setVisibility(View.INVISIBLE);
    }
}