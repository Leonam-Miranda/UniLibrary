package com.example.unilibrary.entities;

import com.example.unilibrary.enums.Language;

public class User {
    private Integer id;
    private String name;
    private byte[] ProfilePic;
    private String email;
    private String password;
    private Language language;

    public User(){}

    public User (Integer id, String name, String email, String password, Language language){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.language = language;
    }

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
    public Language getLanguage(){
        return language;
    }
    public void setLanguage(Language language){
        this.language = language;
    }
}
