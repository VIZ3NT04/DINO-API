package org.example.dinoapi.service;

import org.example.dinoapi.model.DinosaurioFavorito;
import org.example.dinoapi.repository.IDinosaurioFavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class IDinosaurioFavoritoServiceImpl implements IDinosaurioFavoritoService {

    @Autowired
    private IDinosaurioFavoritoRepository repository;

    @Override
    public DinosaurioFavorito insertarDinosaurioFavorito(DinosaurioFavorito dinoFavorito) {
        List<DinosaurioFavorito> listaDinosaurioFavorito = repository.findAll();
        int contador = 0;

        for (DinosaurioFavorito dinosaurioFavorito : listaDinosaurioFavorito) {
            if (dinosaurioFavorito.getNombre().equals(dinoFavorito.getNombre())) {
                contador++;
            }
        }

        if (contador >= 5) {
            return null;
        } else {
            return repository.save(dinoFavorito);
        }

    }


    @Override
    public List<DinosaurioFavorito> listarDinosaurioFavorito() {
        return repository.findAll();
    }

    @Override
    public DinosaurioFavorito eliminarDinosaurioFavorito(Integer id) {
        Optional<DinosaurioFavorito> opt = repository.findById(id);
        if (opt.isPresent()) {
            // Obtener la ruta del archivo desde el campo "foto"
            String rutaFoto = opt.get().getFoto();
            if (rutaFoto != null && !rutaFoto.isEmpty()) {
                // Eliminar la imagen si existe
                File fotoFile = new File("." + rutaFoto); // añade "." para que sea relativa a la raíz del proyecto
                if (fotoFile.exists()) {
                    fotoFile.delete();
                }
            }

            // Eliminar de la base de datos
            repository.deleteById(id);
            return opt.get();
        }
        return null;
    }



}
