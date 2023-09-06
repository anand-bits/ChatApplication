package com.example.chartapplication;

public class User {
    String profilepoc,mail,userName,password,userId,lastMessage,status;

    public User(String profilepoc, String mail, String userName, String password, String userId, String status) {
        this.profilepoc = profilepoc;
        this.mail = mail;
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.status = status;
    }

    public String getProfilepoc() {
        return profilepoc;
    }

    public void setProfilepoc(String profilepoc) {
        this.profilepoc = profilepoc;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User(){

    }


}
