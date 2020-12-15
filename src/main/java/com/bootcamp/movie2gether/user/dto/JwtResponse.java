package com.bootcamp.movie2gether.user.dto;

public class JwtResponse {
    private String token;
    private String id;
    private String userName;
    private String email;

    public JwtResponse(String accessToken, String id, String userName, String email){
        this.token = accessToken;
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    public JwtResponse(){}

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getId(){
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(){this.userName = userName;}

    public void setEmail(){this.email = email;}

}
