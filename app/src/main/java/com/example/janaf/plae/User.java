package com.example.janaf.plae;

public class User {

    public String email;
    public String nome;
    public String saldo;
    public boolean admin;
    public String id_pessoa;
    public String image;

    public User(){}

    public User(boolean admin, String email, String id_pessoa, String image, String nome, String saldo) {
        this.email = email;
        this.nome = nome;
        this.saldo = saldo;
        this.admin = admin;
        this.id_pessoa=id_pessoa;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId_pessoa() {
        return id_pessoa;
    }

    public void setId_pessoa(String id_pessoa) {
        this.id_pessoa = id_pessoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}