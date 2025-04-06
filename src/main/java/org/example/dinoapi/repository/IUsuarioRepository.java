package org.example.dinoapi.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.dinoapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.password = :password")
    Usuario loginUsuario(@Param("email") String email, @Param("password") String password);

    Usuario getUsuarioByEmail(@Email @NotBlank(message = "El correo no puede estar vacío") String email);
}
