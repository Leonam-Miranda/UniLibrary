package com.example.unilibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilibrary.R;
import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.service.AuthService;
import com.example.unilibrary.service.Session;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private AuthService auth;
    private int selectedAvatarResId = R.drawable.boypfp; // ← novo
    private final int[] avatars = {                       // ← novo
            R.drawable.boypfp, R.drawable.chickenpfp, R.drawable.dragonpfp,
            R.drawable.monkeypfp, R.drawable.pandapfp, R.drawable.penguinpfp,
            R.drawable.womanpfp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = new AuthService(this);
        setupAvatarSelection();

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

        MaterialAutoCompleteTextView courseAutoComplete = findViewById(R.id.courseAutoComplete);
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
                            user.setCourse(courseAutoComplete.getText().toString()); // salva o curso
                            user.setAvatarResId(selectedAvatarResId); // ← novo
                            new Thread(() ->                           // ← novo
                                    AppDatabase.getInstance(this).userDao().update(user)
                            ).start();
                            Session.save(this, user.getId());
                            Intent intent = new Intent(this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                );
            });
        }
    }
    private void setupAvatarSelection() {
        int[] avatarIds = {
                R.id.avatar1, R.id.avatar2, R.id.avatar3, R.id.avatar4,
                R.id.avatar5, R.id.avatar6, R.id.avatar7
        };

        ImageView ivSelected = findViewById(R.id.ivSelectedAvatar);

        for (int i = 0; i < avatarIds.length; i++) {
            final int index = i;
            MaterialCardView card = findViewById(avatarIds[i]);
            if (card == null) continue;

            card.setOnClickListener(v -> {
                for (int id : avatarIds) {
                    MaterialCardView c = findViewById(id);
                    if (c != null) c.setStrokeColor(
                            getResources().getColor(R.color.background_light_grey));
                }
                card.setStrokeColor(getResources().getColor(R.color.primary_red));
                selectedAvatarResId = avatars[index];
                ivSelected.setImageResource(selectedAvatarResId);
            });
        }
    }
}
