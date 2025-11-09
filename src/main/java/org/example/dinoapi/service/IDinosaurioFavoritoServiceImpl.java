package org.example.dinoapi.service;

import org.example.dinoapi.model.DinosaurioFavorito;
import org.example.dinoapi.repository.IDinosaurioFavoritoRepository;
import org.example.dinoapi.utils.GithubUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class IDinosaurioFavoritoServiceImpl implements IDinosaurioFavoritoService {

    @Autowired
    private IDinosaurioFavoritoRepository repository;

    @Autowired
    private GithubUploader uploader;

    @Override
    public int contarPorEmail(String email) {
        return repository.countDinosaurioFavoritoByEmailUsuario(email);
    }

    @Override
    public List<DinosaurioFavorito> buscarPorEmail(String email) {
        List<DinosaurioFavorito> lista = repository.getDinosaurioFavoritosByEmailUsuario(email);
        for (DinosaurioFavorito dino : lista) {
            // Coger la foto de Github y pasarla a base64
            String base64 = uploader.descargar(dino.getFoto());
            dino.setFoto(base64);
        }

        return lista;
    }

    @Override
    public DinosaurioFavorito actualizarDinosaurioFavorito(DinosaurioFavorito dinoFavorito) {
        return repository.save(dinoFavorito);
    }

    @Override
    public void deleteDinosauriosFavorito(String email) {
        repository.deleteDinosaurioFavoritoByEmailUsuario(email);
    }

    @Override
    public DinosaurioFavorito insertarDinosaurioFavorito(DinosaurioFavorito dinoFavorito) {
        List<DinosaurioFavorito> listaDinosaurioFavorito = repository.findAll();
        int contador = 0;

        for (DinosaurioFavorito dinosaurioFavorito : listaDinosaurioFavorito) {
            if (dinosaurioFavorito.getEmailUsuario().equals(dinoFavorito.getEmailUsuario())) {
                contador++;
            }
        }

        if (contador >= 5) {
            return null;
        }

        return repository.save(dinoFavorito);
    }

    @Override
    public List<DinosaurioFavorito> listarDinosaurioFavorito() {
        return repository.findAll();
    }

    @Override
    public DinosaurioFavorito eliminarDinosaurioFavorito(Integer id) {
        Optional<DinosaurioFavorito> opt = repository.findById(id);
        if (opt.isPresent()) {
            String rutaFoto = opt.get().getFoto();
            if (rutaFoto != null && !rutaFoto.isEmpty()) {

                File fotoFile = new File("." + rutaFoto);
                if (fotoFile.exists()) {
                    fotoFile.delete();
                }
            }

            repository.deleteById(id);
            return opt.get();
        }
        return null;
    }
}
