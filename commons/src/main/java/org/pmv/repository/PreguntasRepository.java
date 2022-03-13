package org.pmv.repository;

import java.util.List;

public interface PreguntasRepository {

    void savePreguntas(List<String> preguntas);
    List<String> getPreguntas(Long examenId);
}
