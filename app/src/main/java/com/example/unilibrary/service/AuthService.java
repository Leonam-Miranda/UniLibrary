package com.example.unilibrary.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.core.util.Consumer;

import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.db.dao.UserDao;
import com.example.unilibrary.model.User;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UserDao dao;
    private final Handler mainThread =
            new Handler(Looper.getMainLooper());

    public AuthService(Context ctx){
        dao = AppDatabase.getInstance(ctx).userDao();
    }

    public void register(String name, String email, String password,
                         Consumer<String> onError,
                         Consumer<User> onSucess){
        if (name.trim().isEmpty() || email.trim().isEmpty()){
            onError.accept("Preencha todos os campos");
            return;
        }
        if (password.length() < 6){
            onError.accept("A senha deve ter pelo menos 6 caracteres");
            return;
        }

        new Thread(() -> {
            if (dao.searchForEmailSync(email) != null){
                mainThread.post(() ->
                        onError.accept("Email já cadastrado"));
                return;
            }
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            User u = new User(name.trim(), email.trim(), hash);
            long id = dao.insert(u);
            u.setId((int) id);
            mainThread.post(() -> onSucess.accept(u));
        }).start();
    }

    public void login(String email, String senha,
                      Consumer<String> onError,
                      Consumer<User> onSucesso){
        if(email.trim().isEmpty() || senha.isEmpty()){
            onError.accept("Preencha todos os campos");
            return;
        }


        new Thread(() -> {
            User u = dao.searchForEmailSync(email.trim());
            if(u == null){
                mainThread.post(() ->
                        onError.accept("Usuário não encontrado"));
                return;
            }
            if(!BCrypt.checkpw(senha, u.getPasswordHash())){
                mainThread.post(() ->
                        onError.accept("Senha incorreta"));
                return;
            }
            mainThread.post(() -> onSucesso.accept(u));
        }).start();
    }
}
