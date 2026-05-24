package com.example.unilibrary.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.unilibrary.model.Loan;

import java.util.List;

// db/LoanDao.java
@Dao
public interface LoanDao {

    @Insert
    long add(Loan loan);

    @Update
    void update(Loan loan);

    @Query("SELECT * FROM loan WHERE userId = :userId AND returned = 0")
    LiveData<List<Loan>> findAssets(int userId);

    @Query("SELECT COUNT(*) FROM loan WHERE userId = :userId AND returned = 0")
    int accountActives(int userId);   // para checar limite de 3

    @Query("SELECT * FROM loan WHERE userId = :userId")
    LiveData<List<Loan>> history(int userId);

    @Query("SELECT * FROM loan WHERE id = :id")
    Loan searchById(int id);

    @Query("SELECT * FROM loan WHERE bookId = :bookId AND returned = 0 LIMIT 1")
    Loan searchByActivesBook(int bookId);
}
