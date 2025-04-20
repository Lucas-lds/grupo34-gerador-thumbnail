package com.fiap.geradorThumbnail.core.domain;

public class Usuario {
    private Long idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cognitoUserId;

    public Usuario(Long idUsuario, String nome, String email, String senha, String telefone) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public Usuario(Long idUsuario){
        this.idUsuario = idUsuario;
    }

    public String getCognitoUserId() {
        return cognitoUserId;
    }

    public void setCognitoUserId(String cognitoUserId) {
        this.cognitoUserId = cognitoUserId;
    }    

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTelefone() {
        return telefone;
    }

}
