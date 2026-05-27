package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;
import com.example.unilibrary.service.AuthService;
import com.example.unilibrary.service.Session;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AuthService auth = new AuthService(this);

        TextInputLayout emailLayout = findViewById(R.id.emailLayout);
        TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(v -> {
            String email = emailLayout.getEditText().getText().toString();
            String senha = passwordLayout.getEditText().getText().toString();

            auth.login(email, senha,
                    erro -> Toast.makeText(this, erro, Toast.LENGTH_SHORT).show(),
                    user -> {
                        Session.save(this, user.getId());
                        startActivity(new Intent(this, DashboardActivity.class));
                        finish();
                    }
            );
        });

        MaterialButton btnGoToRegister = findViewById(R.id.btnGoToRegister);
        if (btnGoToRegister != null)
            btnGoToRegister.setOnClickListener(v ->
                    startActivity(new Intent(this, RegisterActivity.class)));

        findViewById(R.id.tvForgotPassword).setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }
}