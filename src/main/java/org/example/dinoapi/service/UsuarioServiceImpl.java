package org.example.dinoapi.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.dinoapi.model.Usuario;
import org.example.dinoapi.model.dto.UsuarioRequestDTO;
import org.example.dinoapi.repository.IUsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
public class UsuarioServiceImpl implements IUsuarioService{
    @Autowired
    private IUsuarioRepository repo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Usuario insertUser(UsuarioRequestDTO usuario) {
        try {

            Usuario u = mapper.map(usuario, Usuario.class);
            String codigo = String.format("%06d", new Random().nextInt(999999));
            u.setCodigoVerificacion(codigo);
            u.setVerificado(false);

            String rawPassword = usuario.getPassword();
            String encryptedPassword = passwordEncoder.encode(rawPassword);
            u.setPassword(encryptedPassword);

            emailService.enviarCodigoVerificacion(u.getEmail(), u.getCodigoVerificacion());
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
    public Usuario modificarUser(UsuarioRequestDTO usuarioDto) {
        Usuario existingUser = repo.getUsuarioByEmail(usuarioDto.getEmail());
        if (existingUser == null) {
            throw new EntityNotFoundException("Usuario no encontrado con el email: " + usuarioDto.getEmail());
        }

        existingUser.setName(usuarioDto.getName());

        if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isBlank()) {
            String encryptedPassword = passwordEncoder.encode(usuarioDto.getPassword());
            existingUser.setPassword(encryptedPassword);
        }

        return repo.save(existingUser);
    }


    @Override
    public void deleteUser(String email) {
        repo.deleteUsuarioByEmail(email);
    }

    @Override
    public Usuario loginUsuario(String email, String password) {
        Usuario user = repo.getUsuarioByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Usuario autenticado: " + user.getName());
            return user;
        } else {
            System.out.println("Credenciales incorrectas.");
            return null;
        }
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
