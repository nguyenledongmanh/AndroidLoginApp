package com.example.usermanagementproject.Modules;

public class UserConvert {
    private int user_image;
    private String full_name, email, phone_number, password;

    public UserConvert() {
    }

    public UserConvert(int user_image, String full_name, String email, String phone_number, String password) {
        this.user_image = user_image;
        this.full_name = full_name;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
    }

    public int getUser_image() {
        return user_image;
    }

    public void setUser_image(int user_image) {
        this.user_image = user_image;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
