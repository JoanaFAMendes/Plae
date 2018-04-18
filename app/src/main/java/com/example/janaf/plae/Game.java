package com.example.janaf.plae;

public class Game {

    public String nome_jogo;
    public String descricao;
    public String preco;
    public String icon;
    public String id_jogo;

    public Game(){}

    public Game(String descricao, String icon, String id_jogo, String nome_jogo, String preco) {
        this.nome_jogo = nome_jogo;
        this.descricao = descricao;
        this.preco = preco;
        this.icon = icon;
        this.id_jogo = id_jogo;
    }

    public String getId() {
        return id_jogo;
    }

    public void setId(String id_jogo) {
        this.id_jogo = id_jogo;
    }

    public String getNome_jogo() {
        return nome_jogo;
    }

    public void setNome_jogo(String nome_jogo) {
        this.nome_jogo = nome_jogo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
