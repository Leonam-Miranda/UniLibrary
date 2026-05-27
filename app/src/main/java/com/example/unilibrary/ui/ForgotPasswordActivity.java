package com.example.unilibrary.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.unilibrary.R;
import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.db.dao.UserDao;
import com.example.unilibrary.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import org.mindrot.jbcrypt.BCrypt;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout emailLayout;
    private TextInputLayout newPasswordLayout;
    private MaterialButton btnAction;
    private UserDao userDao;
    private User foundUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userDao = AppDatabase.getInstance(this).userDao();

        emailLayout = findViewById(R.id.emailLayout);
        newPasswordLayout = findViewById(R.id.newPasswordLayout);
        btnAction = findViewById(R.id.btnAction);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnAction.setOnClickListener(v -> {
            if (foundUser == null) {
                verifyEmail();
            } else {
                resetPassword();
            }
        });
    }

    private void verifyEmail() {
        String email = emailLayout.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Digite seu e-mail", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            foundUser = userDao.searchForEmailSync(email);
            runOnUiThread(() -> {
                if (foundUser != null) {
                    emailLayout.setEnabled(false);
                    newPasswordLayout.setVisibility(View.VISIBLE);
                    btnAction.setText("Redefinir Senha");
                    Toast.makeText(this, "E-mail verificado! Digite a nova senha.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "E-mail não encontrado.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void resetPassword() {
        String newPassword = newPasswordLayout.getEditText().getText().toString();
        if (newPassword.length() < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            foundUser.setPasswordHash(hash);
            userDao.update(foundUser);
            runOnUiThread(() -> {
                Toast.makeText(this, "Senha atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
