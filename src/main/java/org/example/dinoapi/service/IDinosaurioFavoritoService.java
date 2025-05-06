package org.example.dinoapi.service;

import org.example.dinoapi.model.DinosaurioFavorito;

import java.util.List;

public interface IDinosaurioFavoritoService {
    DinosaurioFavorito insertarDinosaurioFavorito(DinosaurioFavorito dinoFavorito);
    List<DinosaurioFavorito> listarDinosaurioFavorito();
    DinosaurioFavorito eliminarDinosaurioFavorito(Integer id);
}
