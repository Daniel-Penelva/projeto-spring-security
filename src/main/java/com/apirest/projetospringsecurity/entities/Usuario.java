package com.apirest.projetospringsecurity.entities;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario WHERE u.email=:email")
@Entity
@Data
@DynamicUpdate
@DynamicInsert
@Table(name = "usuarios")
public class Usuario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String telefone;
    private String email;
    private String password;
    private String status;
    private String role;

}