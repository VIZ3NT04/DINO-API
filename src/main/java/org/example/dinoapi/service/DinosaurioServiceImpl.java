package org.example.dinoapi.service;

import org.example.dinoapi.model.Dinosaurio;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.repository.IDinosaurioRepository;
import org.example.dinoapi.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DinosaurioServiceImpl implements IDinosaurioService {
    @Autowired
    private IDinosaurioRepository repo;

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
}
