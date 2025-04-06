package org.example.dinoapi.controller;

import org.example.dinoapi.model.Dinosaurio;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.service.IDinosaurioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dinosaurios")
public class DinosaurioController {

    @Autowired
    private IDinosaurioService service;

    @GetMapping
    public ResponseEntity<List<Dinosaurio>> findAll() {
        List<Dinosaurio> dinosaurios = service.listaDinosaurio();
        if (dinosaurios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(dinosaurios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dinosaurio> findById(@PathVariable("id") Integer id) {
        Dinosaurio dinosaurio = service.listaDinosaurioPorId(id);
        if (dinosaurio == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dinosaurio, HttpStatus.OK);
    }


}
