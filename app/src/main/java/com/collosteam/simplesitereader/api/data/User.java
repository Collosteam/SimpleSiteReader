package com.collosteam.simplesitereader.api.data;

/**
 * Класс реализация основного интерфейса Person
 */
public class User implements Person {

    public User(String login, String passw, String email) {
        this.login = login;
        this.passw = passw;
        this.email = email;
    }

    private String login;

    private String passw;

    private String email;

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPass() {
        return passw;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person user = (Person) o;

        if (!login.equals(user.getLogin())) return false;
        if (!passw.equals(user.getPass())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + passw.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", passw='" + passw + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
