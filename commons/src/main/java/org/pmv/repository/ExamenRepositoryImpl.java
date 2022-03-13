package org.pmv.repository;

import org.pmv.model.Examen;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository{

    @Override
    public List<Examen> findAll() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
