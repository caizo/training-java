package org.pmv.repository;

import lombok.extern.slf4j.Slf4j;
import org.pmv.Data;

import java.util.List;

public class PreguntasRepositoryImpl implements PreguntasRepository{

    @Override
    public void savePreguntas(List<String> preguntas) {

    }

    @Override
    public List<String> getPreguntas(Long examenId) {
        System.out.println("Method 'PreguntasRepositoryImpl.getPreguntas()' real");
        return Data.PREGUNTAS;
    }
}
