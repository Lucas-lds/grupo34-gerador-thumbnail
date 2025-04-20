package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository.entities;
import com.fiap.geradorThumbnail.core.domain.Usuario;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_usuarios")
public class UsuarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nome")
    private String nome;

   @Column(name = "cognito_user_id", nullable = false)
    private String cognitoUserId;     

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "senha")
    private String senha;

    @Column(name = "telefone")
    private String telefone;

    public UsuarioEntity() {
    }

public UsuarioEntity(Long id, String nome, String cognitoUserId, String email, String senha, String telefone) {
    this.id = id;
    this.nome = nome;
    this.cognitoUserId = cognitoUserId;
    this.email = email;
    this.senha = senha;
    this.telefone = telefone;
}

    public Usuario toDomain() {
        Usuario usuario = new Usuario(id, nome, email, senha, telefone);
        usuario.setCognitoUserId(cognitoUserId);
        return usuario;
    }

    public static UsuarioEntity fromDomain(Usuario usuario) {
        return new UsuarioEntity(usuario.getIdUsuario(), usuario.getNome(), usuario.getCognitoUserId(), usuario.getEmail(), usuario.getSenha(), usuario.getTelefone());
    }

    public String getCognitoUserId() {
        return cognitoUserId;
    }

    public void setCognitoUserId(String cognitoUserId) {
        this.cognitoUserId = cognitoUserId;
    }

}
