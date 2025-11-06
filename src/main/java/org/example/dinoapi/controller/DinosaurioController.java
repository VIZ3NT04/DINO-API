package org.example.dinoapi.controller;

import org.example.dinoapi.model.Dinosaurio;
import org.example.dinoapi.service.IDinosaurioService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
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

    @GetMapping("/search")
    public ResponseEntity<List<Dinosaurio>> findByName(@RequestParam("name") String name) {
        List<Dinosaurio> dinosaurios = service.listaDinosaurioPorName(name);
        if (dinosaurios == null || dinosaurios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dinosaurios, HttpStatus.OK);
    }

    @GetMapping("/generar-aleatorio-imagen")
    public ResponseEntity<Dinosaurio> generarDinoCompletoConImagen() {
        Dinosaurio dino = service.generarDinosaurioAleatorio();
        String tipo = dino.getTipo();
        String caracteristicas = "";

        switch (tipo.toLowerCase()) {
            case "carnívoro":
                caracteristicas = "sharp teeth, claws, aggressive posture";
                break;
            case "herbívoro":
                caracteristicas = "like a Triceratops , Stegosaurus or Diplodocus";
                break;
            case "omnivoro":
                caracteristicas = "mixed features, curious expression, moderate size, flexible limbs";
                break;
            default:
                caracteristicas = "generic dinosaur features";
        }

        String prompt = "a fantasy " + tipo + " dinosaur named " + dino.getNombre() +
                ", " + caracteristicas + ", prehistoric jungle, " + dino.getPeriodo().getNombre() +
                " period, cartoon style, dynamic pose, vivid colors, detailed textures, fierce expression, sharp teeth and claws, strong muscles, dramatic lighting, cinematic cartoon illustration, full body";
                //" period, cartoon style, full body, vivid colors, dynamic pose, max four limbs";


        byte[] imagenBytes = service.generarImagenComoBytes(prompt);

        if (imagenBytes == null) {
            throw new RuntimeException("La generación de imagen falló: no se recibió ningún dato.");
        }

        String base64Imagen = Base64.getEncoder().encodeToString(imagenBytes);

        dino.setFoto(base64Imagen);

        return new ResponseEntity<>(dino, HttpStatus.OK);
    }

    @GetMapping("/paginar")
    public ResponseEntity<List<Dinosaurio>> listarDinosauriosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Dinosaurio> pagina = service.listarDinosauriosPaginados(page, size);
        if (pagina.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pagina.getContent(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<Dinosaurio>> guardarDinosaurios(@RequestBody List<Dinosaurio> dinosaurios) {
        List<Dinosaurio> guardados = dinosaurios.stream()
                .map(d -> service.guardar(d))
                .toList();
        return new ResponseEntity<>(guardados, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        service.deleteDinosaurio(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
