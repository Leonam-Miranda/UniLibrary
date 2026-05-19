package com.example.unilibrary.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnRegister).setOnClickListener(v -> finish());
    }
}