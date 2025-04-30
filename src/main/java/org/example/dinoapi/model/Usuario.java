package org.example.dinoapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String password;

    @Email
    @NotBlank(message = "El correo no puede estar vacío")
    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private boolean verificado;

    @Column
    private String codigoVerificacion;


    public Usuario() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public Usuario(Integer id, String name, String password, String email, boolean verificado, String codigoVerificacion) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.verificado = verificado;
        this.codigoVerificacion = codigoVerificacion;
    }
}
