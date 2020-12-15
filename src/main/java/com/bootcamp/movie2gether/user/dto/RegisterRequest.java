package com.bootcamp.movie2gether.user.dto;

public class RegisterRequest {
    private String userName;
    private String email;
    private String password;

    public RegisterRequest(String userName, String email, String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public RegisterRequest(){}

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(){this.userName = userName;}

    public void setEmail(){this.email = email;}

    public void setPassword(){this.password = password;}

}
