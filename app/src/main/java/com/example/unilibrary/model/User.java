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

    public int readBooks = 0;

    public User(){}

    public User (String name,
                 String email,
                 String passwordHash){
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public byte[] getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        ProfilePic = profilePic;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(@NonNull String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getReadBooks() {
        return readBooks;
    }

    public void setReadBooks(int readBooks) {
        this.readBooks = readBooks;
    }
}
