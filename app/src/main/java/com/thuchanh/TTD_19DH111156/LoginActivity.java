package com.thuchanh.TTD_19DH111156;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText tvUsername, tvPassword;
    AppCompatButton btnSignin, btnSignup;
    TextView alert_login;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        btnSignup = findViewById(R.id.btn_signup);
        btnSignin = findViewById(R.id.btn_signin);
        alert_login = findViewById(R.id.tv_alert_login);

        alert_login.setVisibility(View.INVISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValid()){
                    firebaseAuth.signInWithEmailAndPassword(tvUsername.getText().toString(), tvPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }else{
                                alert_login.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }}
        });
    }
    private boolean checkValid() {
        if (tvUsername.getText().toString().matches("") || tvPassword.getText().toString().matches("")){
            checkVisibility();
            return false;}
        uncheckVisibility();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void checkVisibility() {
        alert_login.setVisibility(View.VISIBLE);
    }

    private void uncheckVisibility() {
        alert_login.setVisibility(View.INVISIBLE);
    }
}