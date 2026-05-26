package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;
import com.example.unilibrary.service.AuthService;
import com.example.unilibrary.service.Session;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private AuthService auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = new AuthService(this);

        // Configuração do Dropdown de Cursos
        String[] courses = {
                "Psicologia",
                "Ed.fisica",
                "Sistemas de Informação",
                "Eng. Agricola",
                "Odontologia",
                "Enfermagem",
                "Fisioterapia",
                "Arquitetura e Urbanismo",
                "Medicina"
        };

        AutoCompleteTextView courseAutoComplete = findViewById(R.id.courseAutoComplete);
        if (courseAutoComplete != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    courses
            );
            courseAutoComplete.setAdapter(adapter);
        }

        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> {
                TextInputEditText etName = findViewById(R.id.etName);
                TextInputEditText etEmail = findViewById(R.id.etEmail);
                TextInputEditText etPassword = findViewById(R.id.etPassword);

                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                auth.register(name, email, password,
                        erro -> Toast.makeText(this, erro, Toast.LENGTH_SHORT).show(),
                        user -> {
                            Session.save(this, user.getId());
                            Intent intent = new Intent(this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                );
            });
        }
    }
}
