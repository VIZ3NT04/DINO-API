package org.example.dinoapi.repository;

import org.example.dinoapi.model.Dinosaurio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDinosaurioRepository extends JpaRepository<Dinosaurio, Integer> {

}
