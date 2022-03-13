package org.pmv;

import org.pmv.model.Examen;

import java.util.Arrays;
import java.util.List;

public class Data {
    public static final List<Examen> EXAMEN_LIST = Arrays.asList(new Examen(1L, "Mates"),
            new Examen(2L, "Lengua"),
            new Examen(3L, "Historia"));
    public static final List<Examen> EXAMEN_LIST_NULL = Arrays.asList(new Examen(null, "Mates"),
            new Examen(null, "Lengua"),
            new Examen(null, "Historia"));
    public static final List<Examen> EXAMEN_LIST_NETATIVOS = Arrays.asList(new Examen(-1L, "Mates"),
            new Examen(-2L, "Lengua"),
            new Examen(-3L, "Historia"));
    public static final List<String> PREGUNTAS =
            Arrays.asList("Sumas","Restas","Divisiones", "Multiplicaciones", "Fracciones");

    public static final Examen EXAMEN_ID_NULL = new Examen(null,"Física");
    public static final Examen EXAMEN = new Examen(100L,"Química");
}
