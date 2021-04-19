package com.example.mars.data;

public class UserData {
    String strImagePath;
    String firstName;
    String lastName;
    String email;
    String password;

    public UserData(String strImagePath,String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.strImagePath=strImagePath;
    }
    public String getStrImagePath() {
        return strImagePath;
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

    public String getPassword() {
        return password;
    }

}
