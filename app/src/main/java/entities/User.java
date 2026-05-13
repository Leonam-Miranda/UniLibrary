package entities;

import enums.Language;

public class User {
    private Integer id;
    private String name;
    private String ProfilePic;
    private String email;
    private String password;
    private Language language;

    public User (Integer id, String name, String ProfilePic, String email, String password, Language language){
        this.id = id;
        this.name = name;
        this.ProfilePic = ProfilePic;
        this.email = email;
        this.password = password;
        this.language = language;
    }

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getProfilePic(){
        return ProfilePic;
    }
    public void setProfilePic(String ProfilePic){
        this.ProfilePic = ProfilePic;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public Language getLanguage(){
        return language;
    }
    public void setLanguage(Language language){
        this.language = language;
    }
}
