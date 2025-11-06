package org.example.dinoapi.controller;

import org.example.dinoapi.model.Pregunta;
import org.example.dinoapi.repository.IPreguntaRepository;
import org.example.dinoapi.service.IPreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/preguntas")
public class PreguntaController {

    @Autowired
    private IPreguntaService service;

    @Autowired
    private IPreguntaRepository repository;

    @GetMapping
    public List<Pregunta> getTodas() {
        return service.obtenerTodas();
    }

    @GetMapping("/nivel/{nivel}")
    public List<Pregunta> getPorNivel(@PathVariable String nivel) {
        return service.obtenerPorNivel(nivel);
    }

    @PostMapping
    public ResponseEntity<List<Pregunta>> guardarPreguntas(@RequestBody List<Pregunta> preguntas) {
        List<Pregunta> guardadas = preguntas.stream()
                .map(p -> service.guardar(p))
                .toList();
        return new ResponseEntity<>(guardadas, HttpStatus.CREATED);
    }

    @GetMapping("/health")
    public String health() {
        System.out.println("PING FET A:" + new Date());
        return "OK";
    }


}
