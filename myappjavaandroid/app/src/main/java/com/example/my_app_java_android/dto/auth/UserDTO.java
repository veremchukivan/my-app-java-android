package com.example.my_app_java_android.dto.auth;

import java.util.List;

public class UserDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    public void setId(int id) {
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setImage(String image)
    {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}