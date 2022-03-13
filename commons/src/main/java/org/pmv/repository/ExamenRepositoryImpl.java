package org.pmv.repository;

import org.pmv.Data;
import org.pmv.model.Examen;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository{

    @Override
    public List<Examen> findAll() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Data.EXAMEN_LIST;
    }

    @Override
    public Examen saveExamen(Examen examen) {
        System.out.println("Method ExamenRepositoryImpl.saveExamen() real");
        return Data.EXAMEN;
    }
}
