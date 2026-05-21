package com.example.unilibrary.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.unilibrary.model.User;

@Dao
public interface UserDao {
    @Insert
    long insert(User u);

    @Update
    void update(User u);

    @Query("SELECT * FROM user where email = :email LIMIT 1") //get bi banco de dados basicamente
    User searchForEmailSync(String email);

    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> searchForId(int id);

    @Query("SELECT * FROM user WHERE id = :id")
    User findByIdSync(int id);
}
