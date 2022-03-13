package org.pmv.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pmv.model.Examen;
import org.pmv.repository.ExamenRepository;
import org.pmv.repository.ExamenRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {

    @Test
    void findExamenByName() {
        ExamenRepository examenRepository = mock(ExamenRepository.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository);

        List<Examen> data = Arrays.asList(new Examen(1L, "Mates"),
                new Examen(2L, "Lengua"),
                new Examen(3L, "Historia"));
        when(examenRepository.findAll()).thenReturn(data);

        Optional<Examen> examen = examenService.findExamenByName("Mates");
        assertTrue(examen.isPresent());
        assertEquals(1L, examen.orElseThrow().getId());
    }

    @Test
    void find_examen_by_name_empty_list() {
        ExamenRepository examenRepository = mock(ExamenRepository.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository);

        List<Examen> data = Collections.emptyList();
        when(examenRepository.findAll()).thenReturn(data);

        Optional<Examen> examen = examenService.findExamenByName("Mates");
        assertTrue(!examen.isPresent());

    }

}