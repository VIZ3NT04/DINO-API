package org.example.dinoapi.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.model.dto.UsuarioRequestDTO;
import org.example.dinoapi.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private IUsuarioService service;

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return new ResponseEntity<>(service.listaUsuarios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> findById(@PathVariable("id") Integer id) {
        Optional<Usuario> u = Optional.ofNullable(service.getUsuarioById(id));
        if (u.isPresent()) {
            return new ResponseEntity<>(u, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/login")
    public ResponseEntity<Usuario> login(@Valid @RequestParam("email") String email, @Valid @RequestParam("password") String password) {
        System.out.println("Estan probant a loggearse");
        Usuario user = service.loginUsuario(email,password);
        System.out.println(user.getName() + " " + user.getPassword() + " " + user.getEmail());

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!user.isVerificado()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @PostMapping("/verificar")
    public ResponseEntity<String> verificarCodigo(@RequestParam String email, @RequestParam String codigo) {
        Usuario user = service.getUsuarioByEmail(email);
        if (user == null) {
            return new ResponseEntity<>("{\"error\": \"Usuario no encontrado\"}", HttpStatus.NOT_FOUND);
        }

        if (user.getCodigoVerificacion() != null && user.getCodigoVerificacion().equals(codigo)) {
            user.setVerificado(true);
            user.setCodigoVerificacion(null); // elimina el código
            service.modificarUser(user);
            return new ResponseEntity<>("{\"message\": \"Verificado correctamente\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"error\": \"Código incorrecto\"}", HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody UsuarioRequestDTO usuario) {
        System.out.println("Estan intentant registrarse");
        Usuario u = service.getUsuarioByEmail(usuario.getEmail());

        if (u == null) {
            Usuario user = service.insertUser(usuario);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @PutMapping
    public ResponseEntity<Usuario> modificar(@Valid @RequestBody Usuario usuario) {
        Usuario user = service.modificarUser(usuario);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id){
        service.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
