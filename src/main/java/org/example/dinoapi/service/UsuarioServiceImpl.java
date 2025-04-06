package org.example.dinoapi.service;

import jakarta.transaction.Transactional;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.model.dto.UsuarioRequestDTO;
import org.example.dinoapi.repository.IUsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService{
    @Autowired
    private IUsuarioRepository repo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Usuario insertUser(UsuarioRequestDTO usuario) {
        try {
            Usuario u = mapper.map(usuario, Usuario.class);
            return repo.save(u);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Error de concurrencia al insertar el usuario", e);
        }
    }

    @Override
    public Usuario modificarUser(Usuario usuario) {
        return repo.save(usuario);
    }

    @Override
    public void deleteUser(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Usuario loginUsuario(String email, String password) {
        Usuario user = repo.loginUsuario(email, password);
        if (user != null) {
            System.out.println("Usuario encontrado: " + user.getName());
        } else {
            System.out.println("Usuario no encontrado o credenciales incorrectas.");
        }
        return user;
    }

    @Override
    public Usuario listarUsuarioPorId(Integer id) {
        return repo.getReferenceById(id);
    }

    @Override
    public List<Usuario> listaUsuarios() {
        return repo.findAll();
    }

    @Override
    public Usuario getUsuarioByEmail(String email) {
        Usuario u = repo.getUsuarioByEmail(email);
        return u;
    }

    @Override
    public Usuario getUsuarioById(Integer id) {
        Usuario u = repo.getReferenceById(id);
        return u;
    }
}
