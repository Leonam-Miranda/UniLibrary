package com.example.unilibrary.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.unilibrary.enums.Language;
@Entity(tableName = "usuario")
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
    private String password;
    
    public int readBooks = 0;

    public User(){}

    public User (String name,
                 String email,
                 String password){
        this.name = name;
        this.email = email;
        this.password = password;}

    public Integer getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public byte[] getProfilePic(){
        return ProfilePic;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
}
