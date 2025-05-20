package org.example.dinoapi.controller;

import org.example.dinoapi.model.DinosaurioFavorito;
import org.example.dinoapi.model.dto.DinosaurioFavoritoRequestDTO;
import org.example.dinoapi.service.IDinosaurioFavoritoService;
import org.example.dinoapi.utils.SupabaseUploader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dinosaurios_favoritos")
public class DinosaurioFavoritoController {

    @Autowired
    private IDinosaurioFavoritoService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SupabaseUploader uploader;


    @PostMapping
    public DinosaurioFavorito crearDinosaurioFavorito(@RequestBody DinosaurioFavoritoRequestDTO dto) {
        DinosaurioFavorito dino = modelMapper.map(dto, DinosaurioFavorito.class);
        return service.insertarDinosaurioFavorito(dino);
    }

    @GetMapping
    public List<DinosaurioFavorito> listarDinosauriosFavoritos() {
        return service.listarDinosaurioFavorito();
    }

    @GetMapping("/{email}")
    public List<DinosaurioFavorito> listarDinosauriosFavoritos(@PathVariable String email) {
        return service.buscarPorEmail(email);
    }

    @DeleteMapping("/{id}")
    public DinosaurioFavorito eliminarDinosaurioFavorito(@PathVariable Integer id) {
        return service.eliminarDinosaurioFavorito(id);
    }

    @GetMapping("/espacio/{email}")
    public ResponseEntity<?> obtenerEspacioDisponible(@PathVariable String email) {
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

        // 1. Crear objeto sin foto para obtener ID
        DinosaurioFavorito dino = new DinosaurioFavorito();
        dino.setNombre(nombre);
        dino.setTipo(tipo);
        dino.setPeriodo(periodo);
        dino.setEmailUsuario(emailUsuario);
        dino.setFoto(""); // temporal

        dino = service.insertarDinosaurioFavorito(dino); // Guarda y obtiene ID

        if (dino == null) {
            return ResponseEntity
                    .badRequest()
                    .body("El usuario ya tiene 5 dinosaurios favoritos.");
        }


        String fileName = dino.getId() + ".png";

        // 🔁 Subir imagen a Supabase
        String publicUrl = uploader.subir(foto, fileName);

        dino.setFoto(publicUrl);
        dino = service.actualizarDinosaurioFavorito(dino);

        return ResponseEntity.ok(dino);
    }
}
