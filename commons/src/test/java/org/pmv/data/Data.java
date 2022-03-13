package org.pmv.data;

import org.pmv.model.Examen;

import java.util.Arrays;
import java.util.List;

public class Data {
    public static final List<Examen> EXAMEN_LIST = Arrays.asList(new Examen(1L, "Mates"),
            new Examen(2L, "Lengua"),
            new Examen(3L, "Historia"));

    public static final List<String> PREGUNTAS_MATES =
            Arrays.asList("Sumas","Restas","Divisiones", "Multiplicaciones", "Fracciones");
}
