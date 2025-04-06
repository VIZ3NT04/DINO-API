package org.example.dinoapi.model;

import jakarta.persistence.*;

@Entity
public class Dinosaurio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private String significado_nombre;

    private String tipo;

    @ManyToOne
    @JoinColumn(name = "periodo_id")
    private Periodo periodo;

    private float longitud;

    private float peso;

    private String foto;

    private String descripcion;

    private String ubicacion;

    public Periodo getPeriodo() {
        return periodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSignificado_nombre() {
        return significado_nombre;
    }

    public void setSignificado_nombre(String significado_nombre) {
        this.significado_nombre = significado_nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicaion() {
        return ubicacion;
    }

    public void setUbicaion(String ubicaion) {
        this.ubicacion = ubicaion;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Dinosaurio() {
    }

    public Dinosaurio(Integer id, String nombre, String significado_nombre, String tipo, Periodo periodo, float longitud, float peso, String foto, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.significado_nombre = significado_nombre;
        this.tipo = tipo;
        this.periodo = periodo;
        this.longitud = longitud;
        this.peso = peso;
        this.foto = foto;
        this.descripcion = descripcion;
    }
}
