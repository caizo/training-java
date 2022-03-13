package org.pmv.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pmv.data.Data;
import org.pmv.model.Examen;
import org.pmv.repository.ExamenRepository;
import org.pmv.repository.PreguntasRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {

    ExamenRepository examenRepository;
    ExamenService examenService;
    PreguntasRepository preguntasRepository;

    @BeforeEach
    void setUp() {
        examenRepository = mock(ExamenRepository.class);
        preguntasRepository = mock(PreguntasRepository.class);
        examenService = new ExamenServiceImpl(examenRepository, preguntasRepository);
    }

    @Test
    void find_examen_by_name() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);

        Optional<Examen> examen = examenService.findExamenByName("Mates");
        assertTrue(examen.isPresent());
        assertEquals(1L, examen.orElseThrow().getId());
    }

    @Test
    void find_examen_by_name_empty_list() {
        List<Examen> data = Collections.emptyList();
        when(examenRepository.findAll()).thenReturn(data);

        Optional<Examen> examen = examenService.findExamenByName("Mates");
        assertFalse(examen.isPresent());
    }


    @Test
    void find_examen_con_preguntas_test() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);
        when(preguntasRepository.getPreguntas(1L)).thenReturn(Data.PREGUNTAS_MATES);

        Examen examenMates = examenService.findExamenConPreguntas("Mates");
        assertEquals(5, examenMates.getPreguntas().size());
    }
}