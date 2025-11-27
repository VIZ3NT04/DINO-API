package org.example.dinoapi.controller;

import org.example.dinoapi.model.DinosaurioFavorito;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.model.dto.DinosaurioFavoritoRequestDTO;
import org.example.dinoapi.service.IDinosaurioFavoritoService;
import org.example.dinoapi.service.IUsuarioService;
import org.example.dinoapi.utils.GithubUploader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dinosaurios_favoritos")
public class DinosaurioFavoritoController {

    @Autowired
    private IDinosaurioFavoritoService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GithubUploader uploader;

    @Autowired
    private IUsuarioService serviceUser;

    @GetMapping
    public ResponseEntity<List<DinosaurioFavorito>> listarDinosauriosFavoritos() {
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = serviceUser.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(service.listarDinosaurioFavorito(), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<DinosaurioFavorito>> listarDinosauriosFavoritos(@PathVariable String email) {
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = serviceUser.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN) && !loggedEmail.equals(email)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(service.buscarPorEmail(email), HttpStatus.OK);
    }
    // REVISAR ESTO , AGARRAR I RECOLLIR EL EMAIL DEL USUARIO A PARTIR DE ID

    @DeleteMapping("/{id}")
    public ResponseEntity<DinosaurioFavorito> eliminarDinosaurioFavorito(@PathVariable Integer id) {

        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = serviceUser.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(service.eliminarDinosaurioFavorito(id), HttpStatus.OK);
    }

    @GetMapping("/espacio/{email}")
    public ResponseEntity<?> obtenerEspacioDisponible(@PathVariable String email) {
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = serviceUser.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN) && !loggedEmail.equals(email)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        int usados = service.contarPorEmail(email);
        int disponibles = 5 - usados;
        return ResponseEntity.ok(disponibles);
    }


    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<?> subirDinosaurioConImagen(
            @RequestParam("nombre") String nombre,
            @RequestParam("tipo") String tipo,
            @RequestParam("periodo") String periodo,
            @RequestParam("email_usuario") String emailUsuario,
            @RequestParam("foto") MultipartFile foto) throws IOException {

        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = serviceUser.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN) && !loggedEmail.equals(emailUsuario)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        DinosaurioFavorito dino = new DinosaurioFavorito();
        dino.setNombre(nombre);
        dino.setTipo(tipo);
        dino.setPeriodo(periodo);
        dino.setEmailUsuario(emailUsuario);
        dino.setFoto("");

        dino = service.insertarDinosaurioFavorito(dino);
        if (dino == null) {
            return ResponseEntity
                    .badRequest()
                    .body("El usuario ya tiene 5 dinosaurios favoritos.");
        }

        String fileName = dino.getId() + ".png";

        String publicUrl = uploader.subir(foto, fileName);

        dino.setFoto(publicUrl);
        dino = service.actualizarDinosaurioFavorito(dino);

        return ResponseEntity.ok(dino);
    }
}
