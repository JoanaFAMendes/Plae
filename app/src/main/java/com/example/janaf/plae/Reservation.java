package com.example.janaf.plae;

public class Reservation {
    public String email;
    public String nome_jogo;
    public String data;
    public String hora;
    public String id_reserva;

    public Reservation(){}

    public Reservation(String data, String email, String hora, String id_reserva, String nome_jogo) {
        this.email = email;
        this.nome_jogo = nome_jogo;
        this.data = data;
        this.hora = hora;
        this.id_reserva = id_reserva;
    }

    public String getId() {
        return id_reserva;
    }

    public void setId(String id) {
        this.id_reserva = id_reserva;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome_jogo() {
        return nome_jogo;
    }

    public void setNome_jogo(String nome_jogo) {
        this.nome_jogo = nome_jogo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
