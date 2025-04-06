package org.example.dinoapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Periodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private float inicio;

    private float fin;

    private String descripcion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getInicio() {
        return inicio;
    }

    public void setInicio(float inicio) {
        this.inicio = inicio;
    }

    public float getFin() {
        return fin;
    }

    public void setFin(float fin) {
        this.fin = fin;
    }




    public Periodo() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Periodo(Integer id, String nombre, float inicio, float fin, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
        this.descripcion = descripcion;
    }
}
