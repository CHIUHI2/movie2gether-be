package com.bootcamp.movie2gether.user.dto;

public class RegisterResponse {
    private String id;
    private String userName;
    private String email;
    private String password;

    public RegisterResponse(String id, String userName, String email, String password){
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public RegisterResponse(){}

    public String getId(){
        return id;
    }

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
