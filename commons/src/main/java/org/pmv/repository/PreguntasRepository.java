package org.pmv.repository;

import java.util.List;

public interface PreguntasRepository {

    List<String> getPreguntas(Long examenId);
}
