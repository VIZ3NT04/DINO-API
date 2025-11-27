package org.example.dinoapi.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.model.dto.UsuarioRequestDTO;
import org.example.dinoapi.security.JwtService;
import org.example.dinoapi.service.IDinosaurioFavoritoService;
import org.example.dinoapi.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    @Autowired
    private IUsuarioService service;

    @Autowired
    private IDinosaurioFavoritoService dino_service;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return new ResponseEntity<>(service.listaUsuarios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> findById(@PathVariable("id") Integer id) {
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = service.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        Optional<Usuario> u = Optional.ofNullable(service.getUsuarioById(id));
        if (u.isPresent()) {
            return new ResponseEntity<>(u, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestParam("email") String email,
            @Valid @RequestParam("password") String password
    ) {
        Usuario user = service.loginUsuario(email, password);

        if (user == null) {
            return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(Map.of("token", token));
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

    @PutMapping("/{email}")
    public ResponseEntity<Usuario> modificar(@PathVariable("email") String email, @Valid @RequestBody UsuarioRequestDTO usuario) {
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = service.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN) && !loggedEmail.equals(email)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        usuario.setEmail(email);
        Usuario user = service.modificarUser(usuario);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }



    @Transactional
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> eliminar(@PathVariable("email") String email){
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = service.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN) && !loggedEmail.equals(email)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        dino_service.deleteDinosauriosFavorito(email);
        service.deleteUser(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
