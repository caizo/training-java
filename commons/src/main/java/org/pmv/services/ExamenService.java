package org.pmv.services;

import org.pmv.model.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenByName(String name);
    Examen findExamenConPreguntas(String name);
}
