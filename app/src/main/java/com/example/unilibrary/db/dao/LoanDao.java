package com.example.unilibrary.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.unilibrary.model.Loan;

import java.util.List;

@Dao
public interface LoanDao {
    @Insert
    long insert(Loan loan);
    @Update
    void update(Loan loan);
    @Delete
    void delete(Loan loan);

    //lista os emprestimos do user
    @Query("SELECT * FROM loan WHERE userId = :userId")
    LiveData<List<Loan>> getByUser(int userId);
    //lista os emprestimos do livro
    @Query("SELECT * FROM loan WHERE bookId = :bookId")
    LiveData<List<Loan>> getByBook(int bookId);

    //emprestimos nao retornados(returnDate nula = ainda em aberto)
    @Query("SELECT * FROM loan WHERE userId = :userId AND returnDate IS NULL")
    LiveData<List<Loan>> getActiveLoansByUser(int userId);
    // Busca de imprestimo por ID
    @Query("SELECT * FROM loan WHERE id = :id")
    Loan findByIdSync(int id);
}
