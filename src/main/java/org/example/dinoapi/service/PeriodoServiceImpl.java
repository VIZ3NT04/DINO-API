package org.example.dinoapi.service;

import org.example.dinoapi.model.Periodo;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.repository.IPeriodoRepository;
import org.example.dinoapi.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeriodoServiceImpl implements IPeriodoService {
    @Autowired
    private IPeriodoRepository repo;

    @Override
    public List<Periodo> listaPeriodos() {
        return repo.findAll();
    }

    @Override
    public Periodo listaPeriodosPorId(Integer id) {
        return repo.getReferenceById(id);
    }
}
