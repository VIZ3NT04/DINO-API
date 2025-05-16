package org.example.dinoapi.repository;

import org.example.dinoapi.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPreguntaRepository extends JpaRepository<Pregunta, Integer> {
    List<Pregunta> findByNivel(String nivel);
}
