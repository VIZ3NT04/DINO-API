package org.example.dinoapi.repository;

import org.example.dinoapi.model.DinosaurioFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDinosaurioFavoritoRepository extends JpaRepository<DinosaurioFavorito, Integer> {

    int countDinosaurioFavoritoByEmailUsuario(String email);

    List<DinosaurioFavorito> getDinosaurioFavoritosByEmailUsuario(String emailUsuario);
    void deleteDinosaurioFavoritoByEmailUsuario(String emailUsuario);
}
