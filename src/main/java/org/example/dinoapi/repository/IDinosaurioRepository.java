package org.example.dinoapi.repository;

import org.example.dinoapi.model.Dinosaurio;
import org.example.dinoapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IDinosaurioRepository extends JpaRepository<Dinosaurio, Integer> {
    @Query("SELECT d FROM Dinosaurio d WHERE LOWER(d.nombre) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Dinosaurio> listarDinosaurioPorNombre(@Param("name") String name);
}
