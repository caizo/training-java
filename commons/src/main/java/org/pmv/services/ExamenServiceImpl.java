package org.pmv.services;

import org.pmv.model.Examen;
import org.pmv.repository.ExamenRepository;
import org.pmv.repository.PreguntasRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{

    private ExamenRepository examenRepository;
    private PreguntasRepository preguntasRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntasRepository preguntasRepository) {
        this.examenRepository = examenRepository;
        this.preguntasRepository = preguntasRepository;
    }

    @Override
    public Optional<Examen> findExamenByName(String name) {
        return examenRepository.findAll().stream()
                .filter(e -> e.getNombre().equals(name))
                .findFirst();
    }


    @Override
    public Examen findExamenConPreguntas(String name) {
        Optional<Examen> examenByName = findExamenByName(name);
        Examen examen = null;
        if(examenByName.isPresent()){
            examen = examenByName.orElseThrow();
            List<String> preguntas = preguntasRepository.getPreguntas(examenByName.get().getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }
}
