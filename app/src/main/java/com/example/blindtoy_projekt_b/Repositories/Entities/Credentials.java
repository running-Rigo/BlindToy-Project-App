package com.example.blindtoy_projekt_b.Repositories.Entities;

public class Credentials {
    private String apiToken;
    private String userId;

    public String getApiToken() {
        return apiToken;
    }

    public String getUserId() {
        return userId;
    }

    public Credentials(String apiToken, String userId) {
        this.apiToken = apiToken;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "apiToken='" + apiToken + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
