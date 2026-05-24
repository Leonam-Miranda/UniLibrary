package com.example.unilibrary.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.unilibrary.model.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    long insert (Book book);
    @Update
    void update (Book book);
    @Delete
    void delete (Book book);

    @Query("SELECT * FROM book")
    LiveData<List<Book>> getAll();

    @Query("SELECT * FROM book WHERE id = :id")
    Book searchById(int id);

    @Query("SELECT * FROM book WHERE title LIKE '%'|| :title ||'%'")
    LiveData<List<Book>> findByTitle(String title);

    @Query("SELECT * FROM book WHERE  status = :status")
    LiveData<List<Book>> getByStatus(String status);

}
