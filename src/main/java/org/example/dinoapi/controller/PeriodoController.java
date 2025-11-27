package org.example.dinoapi.controller;

import org.example.dinoapi.model.Periodo;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.service.IPeriodoService;
import org.example.dinoapi.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/periodos")
public class PeriodoController {

    @Autowired
    private IPeriodoService service;

    @Autowired
    private IUsuarioService serviceUser;

    @GetMapping
    public ResponseEntity<List<Periodo>> findAll() {
        List<Periodo> periodos = service.listaPeriodos();
        if (periodos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(periodos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<Periodo>> guardarPeriodos(@RequestBody List<Periodo> periodos) {
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = serviceUser.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Periodo> guardados = periodos.stream()
                .map(p -> service.guardar(p))
                .toList();
        return new ResponseEntity<>(guardados, HttpStatus.CREATED);
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
