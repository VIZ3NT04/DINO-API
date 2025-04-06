package org.example.dinoapi.controller;

import org.example.dinoapi.model.Periodo;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.service.IPeriodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/periodos")
public class PeriodoController {

    @Autowired
    private IPeriodoService service;

    @GetMapping
    public ResponseEntity<List<Periodo>> findAll() {
        List<Periodo> periodos = service.listaPeriodos();
        if (periodos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(periodos, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Periodo> findById(@PathVariable("id") Integer id) {
        Periodo periodo = service.listaPeriodosPorId(id);
        if (periodo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(periodo, HttpStatus.OK);
    }
}
