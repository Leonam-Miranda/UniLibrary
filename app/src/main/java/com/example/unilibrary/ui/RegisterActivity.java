package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                // Ao cadastrar, vai para o Dashboard
                Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }
    }
}
