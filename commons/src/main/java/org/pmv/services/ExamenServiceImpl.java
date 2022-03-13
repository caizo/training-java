package org.pmv.services;

import org.pmv.model.Examen;
import org.pmv.repository.ExamenRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{

    private ExamenRepository examenRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public Optional<Examen> findExamenByName(String name) {
        return examenRepository.findAll().stream()
                .filter(e -> e.getNombre().equals(name))
                .findFirst();
    }
}
