package com.example.restapiprojectappdemo.model;

public class ServerResponse {
    private String result;
    private String message;
    private User user;

    public ServerResponse() {
    }

    public ServerResponse(String result, String message, User user) {
        this.result = result;
        this.message = message;
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
