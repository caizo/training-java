package org.pmv.repository;

import org.pmv.model.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
}
