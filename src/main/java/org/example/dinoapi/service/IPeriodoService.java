package org.example.dinoapi.service;

import org.example.dinoapi.model.Periodo;
import org.example.dinoapi.model.Usuario;

import java.util.List;

public interface IPeriodoService {
    List<Periodo> listaPeriodos();
    Periodo listaPeriodosPorId(Integer id);
    Periodo guardar(Periodo periodo);
}
