package org.example.dinoapi.controller;

import org.example.dinoapi.model.Dinosaurio;
import org.example.dinoapi.service.IDinosaurioService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/dinosaurios")
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
                "roaring towards the sky",
                "walking slowly through vegetation",
                "drinking from a river",
                "emerging from the shadows"
        };

        String color = colores[rand.nextInt(colores.length)];
        String accion = acciones[rand.nextInt(acciones.length)];

        switch (tipo.toLowerCase()) {
            case "carnívoro":
                String[] subCarn = {"raptor-like", "allosaurus-like", "spinosaurus-like", "tyrannosaurus-like"};
                prompt =  "dinosaur named " + dino.getNombre() + " in " + dino.getPeriodo().getNombre() + ", a full-body, photorealistic image of a large " + subCarn[rand.nextInt(subCarn.length)]+ " **bipedal carnivorous dinosaur**, " +
                        " similar in build to a Raptor or Allosaurus. The creature has a muscular body, thick powerful legs, and is poised aggressively. Emphasize **serrated sharp teeth**" +
                        ", strong jaws, and **razor-like claws** on its feet and short arms." +
                        " The skin " + color+ ". Set it in a dense forest, " + accion + " . Highly detailed, cinematic, predator view, one head.";

                break;
            case "herbívoro":
                String[] subHerb = {"triceratops-like", "ankylosaurus-like", "brachiosaurus-like", "stegosaurus-like"};
                prompt = "dinosaur named " + dino.getNombre() + " in " + dino.getPeriodo().getNombre() + " a full-body, photorealistic image of a massive " + subHerb[rand.nextInt(subHerb.length)] + " **quadrupedal herbivorous dinosaur**, " +
                        " similar in scale to a Triceratops or Brachiosaurus. The creature has a thick, armored hide and large," +
                        " sturdy limbs supporting its immense weight. Highlight **protective features** like large spikes, thick plating, or defensive horns on its head and back. " +
                        " The dinosaur is peacefully grazing in an open prehistoric grassland under a warm sun" +
                        ", focused on texture and scale. Extremely detailed, natural lighting, majestic wide-angle shot, one head and four paws," +
                        color + ", " + accion;

                break;
            case "omnívoro":
                String[] subOmn  = {"ornithomimus-like", "iguanodon-like", "small theropod", "hypsilophodon-like"};
                prompt = "dinosaur named " + dino.getNombre() + " in " + dino.getPeriodo().getNombre() + " a full-body, photorealistic image of a mid-sized , " + subOmn[rand.nextInt(subOmn.length)]+ " **omnivorous dinosaur**, " +
                        "agile and robust, with a blend of features for both hunting and foraging. " +
                        "The dinosaur has strong legs for running and a relatively long tail for balance." +
                        " Focus on a versatile mouth structure, showing **small and varied teeth** suitable for meat and plants. " +
                        "The skin " + color + ", perfectly camouflaged in a swampy environment, searching for food, "+ accion + ". High-resolution, detailed scales, subtle wet reflections, one head.";

                break;
            default:
                prompt = "generic dinosaur appearance";
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
