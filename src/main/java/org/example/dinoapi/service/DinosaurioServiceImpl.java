package org.example.dinoapi.service;

import org.example.dinoapi.model.Dinosaurio;
import org.example.dinoapi.model.Periodo;
import org.example.dinoapi.repository.IDinosaurioRepository;
import org.example.dinoapi.repository.IPeriodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Random;

@Service
public class DinosaurioServiceImpl implements IDinosaurioService {
    @Autowired
    private IDinosaurioRepository repo;

    @Autowired
    private IPeriodoRepository periodoRepo;

    private static final String[] TIPOS = {"carnívoro", "herbívoro", "omnívoro"};
    private static final String[] PREFIJOS = {
            "Mega", "Cryo", "Bronto", "Stego", "Veloci", "Giga", "Gita" , "Ptero",
            "Allo", "Spino", "Raptor", "Odoo", "Mosa" , "Enova" , "Plesio"


    };
    private static final String[] SUFIJOS = {
            "saurio", "ceratops", "raptor", "donte", "saurus", "tyrannus", "no"
            , "gnathus", "draco", "ornis", "rex" , "pecia" , "reaper"
    };

    @Override
    public Page<Dinosaurio> listarDinosauriosPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable);
    }


    private String generarNombreInventado() {
        Random random = new Random();
        String prefijo = PREFIJOS[random.nextInt(PREFIJOS.length)];
        String sufijo = SUFIJOS[random.nextInt(SUFIJOS.length)];
        return prefijo + sufijo;
    }


    @Override
    public List<Dinosaurio> listaDinosaurio() {
        return repo.findAll();
    }

    @Override
    public Dinosaurio listaDinosaurioPorId(Integer id) {
        return repo.getReferenceById(id);
    }

    @Override
    public List<Dinosaurio> listaDinosaurioPorName(String name) {
        return repo.listarDinosaurioPorNombre(name);
    }

    public byte[] generarImagenComoBytes(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.IMAGE_PNG));
            headers.set("Authorization", "Bearer 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");

            String body = "{\"inputs\": \"" + prompt + "\"}";
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            String apiUrl = "https://api-inference.huggingface.co/models/stabilityai/stable-diffusion-xl-base-1.0";
            ResponseEntity<byte[]> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, byte[].class);

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public Dinosaurio generarDinosaurioAleatorio() {
        Random rand = new Random();

            String tipo = TIPOS[rand.nextInt(TIPOS.length)];
            Periodo periodo = obtenerPeriodoAleatorio();

            float longitud = 10 + rand.nextFloat() * 20;
            float peso = 500 + rand.nextFloat() * 5000;

            Dinosaurio dino = new Dinosaurio();
            dino.setNombre(generarNombreInventado());
            dino.setTipo(tipo);
            dino.setPeriodo(periodo); // Asignamos el periodo aleatorio aquí
            dino.setLongitud(longitud);
            dino.setPeso(peso);
            dino.setDescripcion("Un dinosaurio de tipo " + tipo + " que vivió en el periodo " + periodo.getNombre() + ".");
            dino.setFoto("");

            return dino;
        }

        private Periodo obtenerPeriodoAleatorio() {
            List<Periodo> periodos = periodoRepo.findAll();
            Random rand = new Random();
            return periodos.get(rand.nextInt(periodos.size()));
        }

        @Override
        public Dinosaurio guardar(Dinosaurio dino) {
            return repo.save(dino);
        }
}
