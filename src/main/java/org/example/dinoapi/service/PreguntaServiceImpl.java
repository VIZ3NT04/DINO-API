package org.example.dinoapi.service;

import org.example.dinoapi.model.Pregunta;
import org.example.dinoapi.repository.IPreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreguntaServiceImpl implements IPreguntaService {
    @Autowired
    private IPreguntaRepository repository;

    @Override
    public List<Pregunta> obtenerPorNivel(String nivel) {
        return repository.findByNivel(nivel);
    }

    @Override
    public List<Pregunta> obtenerTodas() {
        return repository.findAll();
    }

    @Override
    public Pregunta guardar(Pregunta pregunta) {
        return repository.save(pregunta);
    }

}
