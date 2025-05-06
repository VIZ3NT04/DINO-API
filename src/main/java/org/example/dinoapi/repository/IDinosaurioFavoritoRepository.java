package org.example.dinoapi.repository;

import org.example.dinoapi.model.DinosaurioFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDinosaurioFavoritoRepository extends JpaRepository<DinosaurioFavorito, Integer> {

}
