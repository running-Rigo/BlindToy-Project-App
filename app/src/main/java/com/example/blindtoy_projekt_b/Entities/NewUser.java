package com.example.blindtoy_projekt_b.Entities;

public class NewUser {
    private String name;
    private String email;
    private String password;

    //Constructor:


    public NewUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //Getter methods:
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }



    @Override
    public String toString() {
        return "NewUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
