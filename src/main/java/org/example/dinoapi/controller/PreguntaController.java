package org.example.dinoapi.controller;

import org.example.dinoapi.model.Pregunta;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.repository.IPreguntaRepository;
import org.example.dinoapi.service.IPreguntaService;
import org.example.dinoapi.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/preguntas")
public class PreguntaController {

    @Autowired
    private IPreguntaService service;

    @Autowired
    private IUsuarioService serviceUser;

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
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = serviceUser.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

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
