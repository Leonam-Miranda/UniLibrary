package com.example.unilibrary.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @NonNull
    private String name;
    private byte[] ProfilePic;
    @NonNull
    @ColumnInfo(index = true)
    private String email;
    @NonNull
    private String passwordHash;

    private int avatarResId;

    public int readBooks = 0;
    public int savedBooksCount = 0;
    private String savedBookIds = ""; // Armazena IDs como "1,2,5,"

    public User(){}

    public User (String name, String email, String passwordHash){
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    public byte[] getProfilePic() { return ProfilePic; }
    public void setProfilePic(byte[] profilePic) { ProfilePic = profilePic; }

    @NonNull
    public String getEmail() { return email; }
    public void setEmail(@NonNull String email) { this.email = email; }

    @NonNull
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(@NonNull String passwordHash) { this.passwordHash = passwordHash; }

    public int getReadBooks() { return readBooks; }
    public void setReadBooks(int readBooks) { this.readBooks = readBooks; }

    public int getSavedBooksCount() { return savedBooksCount; }
    public void setSavedBooksCount(int savedBooksCount) { this.savedBooksCount = savedBooksCount; }

    public String getSavedBookIds() { return savedBookIds == null ? "" : savedBookIds; }
    public void setSavedBookIds(String savedBookIds) { this.savedBookIds = savedBookIds; }
    public int getAvatarResId() { return avatarResId; }
    public void setAvatarResId(int avatarResId) { this.avatarResId = avatarResId; }

    // Helper para verificar se um livro está salvo
    public boolean isBookSaved(int bookId) {
        return getSavedBookIds().contains(bookId + ",");
    }

    // Helper para adicionar/remover livro
    public void toggleSaveBook(int bookId) {
        String idStr = bookId + ",";
        if (isBookSaved(bookId)) {
            setSavedBookIds(getSavedBookIds().replace(idStr, ""));
            savedBooksCount--;
        } else {
            setSavedBookIds(getSavedBookIds() + idStr);
            savedBooksCount++;
        }
    }
}
