package org.example.dinoapi.service;

import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.model.dto.UsuarioRequestDTO;
import java.util.List;

public interface IUsuarioService {
    Usuario insertUser(UsuarioRequestDTO usuarioDto);
    Usuario modificarUser(Usuario usuario);
    Usuario modificarUser(UsuarioRequestDTO usuario);
    void deleteUser(String id);
    Usuario loginUsuario(String email,  String password);
    List<Usuario> listaUsuarios();
    Usuario getUsuarioByEmail(String email);
    Usuario getUsuarioById(Integer id);

}
