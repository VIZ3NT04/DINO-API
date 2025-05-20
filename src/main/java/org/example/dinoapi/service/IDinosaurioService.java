package org.example.dinoapi.service;

import org.example.dinoapi.model.Dinosaurio;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.model.dto.UsuarioRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDinosaurioService {
    List<Dinosaurio> listaDinosaurio();
    Dinosaurio listaDinosaurioPorId(Integer id);
    List<Dinosaurio> listaDinosaurioPorName(String name);
    Dinosaurio generarDinosaurioAleatorio();

    byte[] generarImagenComoBytes(String prompt);
    Page<Dinosaurio> listarDinosauriosPaginados(int page, int size);
    Dinosaurio guardar(Dinosaurio dino);

}
