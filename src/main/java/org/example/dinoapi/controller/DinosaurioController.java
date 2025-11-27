package org.example.dinoapi.controller;

import org.example.dinoapi.model.Dinosaurio;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.service.IDinosaurioService;
import org.example.dinoapi.service.IUsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/dinosaurios")
public class DinosaurioController {

    @Autowired
    private IDinosaurioService service;

    @Autowired
    private IUsuarioService serviceUser;

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
        String prompt = "";
        Random rand = new Random();

        String[] colores = {
                "green and brown scales",
                "dark grey skin with red streaks",
                "olive skin with yellowish spots",
                "bluish tones on the back and head",
                "tan armor plates with dark stripes"
        };

        String[] acciones = {
                "grazing peacefully",
                "roaring toward the sky",
                "walking slowly through vegetation",
                "drinking from a river",
                "emerging from the shadows"
        };

        String[] entornos = {
                "a misty prehistoric jungle",
                "an open grassland under warm sunlight",
                "a rocky canyon surrounded by ferns",
                "a dense forest near a riverbank",
                "a swampy marsh full of tall plants"
        };

        String color = colores[rand.nextInt(colores.length)];
        String accion = acciones[rand.nextInt(acciones.length)];
        String entorno = entornos[rand.nextInt(entornos.length)];

        switch (tipo.toLowerCase()) {
            case "carnívoro":
                String[] subCarn = {"raptor-like", "allosaurus-like", "spinosaurus-like", "tyrannosaurus-like", "carnotaurus-like"};
                prompt = "A full-body, photorealistic image of a large " + subCarn[rand.nextInt(subCarn.length)] +
                        " carnivorous dinosaur named " + dino.getNombre() +
                        " from the " + dino.getPeriodo().getNombre() + " period. " +
                        "It has " + color + ", powerful legs, strong jaws with serrated teeth, and sharp claws on its short arms. " +
                        "The creature is " + accion + " in " + entorno + ". " +
                        "Highly detailed, cinematic predator lighting, one head.";
                break;
            case "herbívoro":
                String[] subHerb = {"triceratops-like", "ankylosaurus-like", "brachiosaurus-like", "stegosaurus-like"};
                prompt = "A full-body, photorealistic image of a massive " + subHerb[rand.nextInt(subHerb.length)] +
                        " herbivorous dinosaur named " + dino.getNombre() +
                        " from the " + dino.getPeriodo().getNombre() + " period. " +
                        "It has " + color + " and large, sturdy limbs supporting its immense weight. " +
                        "Highlight protective features like spikes, thick plating, or defensive horns. " +
                        "The dinosaur is " + accion + " in " + entorno + ", under natural sunlight. " +
                        "Extremely detailed textures and scales, one head, four legs.";
                break;
            case "omnívoro":
                String[] subOmn = {"ornithomimus-like", "iguanodon-like", "small theropod", "hypsilophodon-like"};
                prompt = "A full-body, photorealistic image of a mid-sized " + subOmn[rand.nextInt(subOmn.length)] +
                        " omnivorous dinosaur named " + dino.getNombre() +
                        " from the " + dino.getPeriodo().getNombre() + " period. " +
                        "It is agile and robust, with strong legs for running and a long tail for balance. " +
                        "It has " + color + ", suitable for both hunting and foraging. " +
                        "The dinosaur is " + accion + " in " + entorno + ". " +
                        "High-resolution, realistic lighting and detailed skin reflections.";
                break;
            default:
                prompt = "A generic full-body photorealistic dinosaur in a prehistoric environment, detailed and cinematic lighting.";
        }

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
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = serviceUser.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Dinosaurio> guardados = dinosaurios.stream()
                .map(d -> service.guardar(d))
                .toList();
        return new ResponseEntity<>(guardados, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario currentUser = serviceUser.getUsuarioByEmail(loggedEmail);
        if (!currentUser.getRole().equals(Usuario.Role.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        service.deleteDinosaurio(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
