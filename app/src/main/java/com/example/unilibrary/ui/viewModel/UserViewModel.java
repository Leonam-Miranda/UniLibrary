package com.example.unilibrary.ui.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.unilibrary.db.AppDatabase;
import com.example.unilibrary.db.dao.UserDao;
import com.example.unilibrary.model.User;

public class UserViewModel extends AndroidViewModel {
    private final UserDao dao;
    private LiveData<User> user;

    public UserViewModel(Application app){
        super(app);
        dao = AppDatabase.getInstance(app).userDao();
    }

    public void init(int userId){
        if(user == null)
            user = dao.searchForId(userId);
    }

    public LiveData<User> getUser(){
        return user;
    }
}
