package org.pmv.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
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
        // Given
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);
        // When
        Optional<Examen> examen = examenService.findExamenByName("Mates");

        // Then
        assertTrue(examen.isPresent());
        assertEquals(1L, examen.orElseThrow().getId());
    }

    @Test
    void find_examen_by_name_empty_list() {
        // Given
        List<Examen> data = Collections.emptyList();
        when(examenRepository.findAll()).thenReturn(data);
        // When
        Optional<Examen> examen = examenService.findExamenByName("Mates");
        // Then
        assertFalse(examen.isPresent());
    }


    @Test
    void find_examen_con_preguntas_test() {
        // Given
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);
        when(preguntasRepository.getPreguntas(1L)).thenReturn(Data.PREGUNTAS);
        // When
        Examen examenMates = examenService.findExamenConPreguntas("Mates");
        // Then
        assertEquals(5, examenMates.getPreguntas().size());
        verify(examenRepository).findAll();
        verify(preguntasRepository).getPreguntas(1L);
    }

    @DisplayName("Este test tiene que fallar")
    @Test
    void find_no_examen_verify_test() {
        // Given
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);
        when(preguntasRepository.getPreguntas(1L)).thenReturn(Data.PREGUNTAS);
        // When
        Examen examen = examenService.findExamenConPreguntas("Tecnología");
        // Then
        assertNull(examen);
        verify(examenRepository).findAll();
        verify(preguntasRepository).getPreguntas(anyLong());
    }


    @Test
    void save_examen_test() {
        /* Dados los siguientes datos. Precondiciones */
        Examen newExamen = Data.EXAMEN;
        newExamen.setPreguntas(Data.PREGUNTAS);

        when(examenRepository.saveExamen(any(Examen.class))).then(new Answer<Examen>(){
            Long examenId = 4L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(examenId++);
                return examen;
            }
        });

        /* When: Cuando se ejecute el siguiente método*/
        Examen examen = examenService.saveExamen(newExamen);


        /* Entonces hacemos las validaciones*/
        assertNotNull(examen);
        assertEquals(4L,examen.getId());

        verify(examenRepository).saveExamen(any(Examen.class));
        verify(preguntasRepository).savePreguntas(anyList());

    }

    @Test
    void find_preguntas_exception_test() {
        // GIVEN
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST_NULL);
        when(preguntasRepository.getPreguntas(isNull())).thenThrow(IllegalArgumentException.class);

        // THEN
        assertThrows(IllegalArgumentException.class,
                () -> this.examenService.findExamenConPreguntas("Mates"));
        verify(examenRepository).findAll();
        verify(preguntasRepository).getPreguntas(isNull());
    }

    @Test
    void argument_matchers_test() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);
        when(preguntasRepository.getPreguntas(anyLong())).thenReturn(Data.PREGUNTAS);

        examenService.findExamenConPreguntas("Mates");

        verify(examenRepository).findAll();
        verify(preguntasRepository).getPreguntas(argThat(arg -> arg != null && arg.equals(1L)));
        verify(preguntasRepository).getPreguntas(eq(1L));
    }
}