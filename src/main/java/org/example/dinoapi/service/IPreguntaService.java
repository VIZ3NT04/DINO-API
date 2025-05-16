package org.example.dinoapi.service;

import org.example.dinoapi.model.Pregunta;

import java.util.List;

public interface IPreguntaService {
    List<Pregunta> obtenerPorNivel(String nivel);
    List<Pregunta> obtenerTodas();
}
