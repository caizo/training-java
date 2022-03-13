package org.pmv.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pmv.data.Data;
import org.pmv.model.Examen;
import org.pmv.repository.ExamenRepository;
import org.pmv.repository.PreguntasRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
    @Mock ExamenRepository examenRepository;
    @Mock PreguntasRepository preguntasRepository;
    @InjectMocks ExamenServiceImpl examenService;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
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
        verify(examenRepository).findAll();
        verify(preguntasRepository).getPreguntas(1L);
    }

    @DisplayName("Este test tiene que fallar")
    @Test
    void find_no_examen_verify_test() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);
        when(preguntasRepository.getPreguntas(1L)).thenReturn(Data.PREGUNTAS_MATES);

        Examen examen = examenService.findExamenConPreguntas("Tecnología");
        assertNull(examen);
        verify(examenRepository).findAll();
        verify(preguntasRepository).getPreguntas(anyLong());
    }
}