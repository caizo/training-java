package org.pmv.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.pmv.Data;
import org.pmv.model.Examen;
import org.pmv.repository.ExamenRepository;
import org.pmv.repository.ExamenRepositoryImpl;
import org.pmv.repository.PreguntasRepository;
import org.pmv.repository.PreguntasRepositoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
    @Mock
    ExamenRepositoryImpl examenRepository;
    @Mock
    PreguntasRepositoryImpl preguntasRepository;
    @InjectMocks ExamenServiceImpl examenService;
    @Captor ArgumentCaptor<Long> captor;

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
        Examen newExamen = Data.EXAMEN_ID_NULL;
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


    @Test
    @DisplayName("argument_matchers_custom_test: tiene que fallar")
    void argument_matchers_custom_test() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST_NETATIVOS);
        when(preguntasRepository.getPreguntas(anyLong())).thenReturn(Data.PREGUNTAS);

        examenService.findExamenConPreguntas("Mates");

        verify(examenRepository).findAll();
        verify(preguntasRepository).getPreguntas(argThat(new MyArgsMatchers()));
    }

    @Test
    void argument_captor_test() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);

        examenService.findExamenConPreguntas("Mates");

        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntasRepository).getPreguntas(captor.capture());

        assertEquals(1L,captor.getValue());

    }

    @Test
    void do_throw_test() {
        Examen examen = new Examen(4L, "Tecnología");
        examen.setPreguntas(Data.PREGUNTAS);

        doThrow(IllegalArgumentException.class).when(preguntasRepository).savePreguntas(anyList());

        assertThrows(IllegalArgumentException.class, () -> examenService.saveExamen(examen));
    }


    @Test
    void do_answer_test() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 1L? Data.PREGUNTAS:Collections.emptyList();
        }).when(preguntasRepository).getPreguntas(anyLong());

        Examen examen = examenService.findExamenConPreguntas("Mates");
        assertEquals(1L, examen.getId());
        assertEquals("Mates", examen.getNombre());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Sumas"));

        verify(preguntasRepository).getPreguntas(anyLong());
    }

    @Test
    void save_examen_with_do_answer_test() {
        /* Dados los siguientes datos. Precondiciones */
        Examen newExamen = Data.EXAMEN_ID_NULL;
        newExamen.setPreguntas(Data.PREGUNTAS);


        doAnswer(invocation -> {
            long examenId = 4L;
            Examen examen = invocation.getArgument(0);
            examen.setId(examenId++);
            return examen;
        }).when(examenRepository).saveExamen(newExamen);

        /* When: Cuando se ejecute el siguiente método*/
        Examen examen = examenService.saveExamen(newExamen);


        /* Entonces hacemos las validaciones*/
        assertNotNull(examen);
        assertEquals(4L,examen.getId());

        verify(examenRepository).saveExamen(any(Examen.class));
        verify(preguntasRepository).savePreguntas(anyList());

    }


    @Test
    void do_call_real_method_test() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);
        //when(preguntasRepository.getPreguntas(anyLong())).thenReturn(Data.PREGUNTAS);
        doCallRealMethod().when(preguntasRepository).getPreguntas(anyLong());

        Examen examen = examenService.findExamenConPreguntas("Mates");

        assertEquals(1L, examen.getId());
        assertEquals("Mates", examen.getNombre());

    }


    @Test
    void spy_test() {
        // spy requiere clases concretas
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntasRepository preguntasRepository = spy(PreguntasRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository,preguntasRepository);

        //when(preguntasRepository.getPreguntas(anyLong())).thenReturn(Data.PREGUNTAS);// real call
        doReturn(Data.PREGUNTAS).when(preguntasRepository).getPreguntas(anyLong());

        Examen examen = examenService.findExamenConPreguntas("Mates");

        assertEquals(1L,examen.getId());
        assertTrue(examen.getPreguntas().contains("Sumas"));

        verify(examenRepository).findAll();
        verify(preguntasRepository).getPreguntas(anyLong());

    }


    @Test
    void order_invocation_test() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);

        Examen examen = examenService.findExamenConPreguntas("Mates");
        Examen examen2 = examenService.findExamenConPreguntas("Lengua");

        InOrder inOrder = inOrder(examenRepository,preguntasRepository);

        // si se invierte el orden fallará
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntasRepository).getPreguntas(1L);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntasRepository).getPreguntas(2L);

    }


    @Test
    void invocations_number_test() {
        when(examenRepository.findAll()).thenReturn(Data.EXAMEN_LIST);

        Examen examen = examenService.findExamenConPreguntas("Mates");

        verify(preguntasRepository,times(1)).getPreguntas(1L);
        verify(preguntasRepository,atLeast(1)).getPreguntas(1L);
        verify(preguntasRepository,atLeastOnce()).getPreguntas(1L);
        verify(preguntasRepository,atMost(11)).getPreguntas(1L);
        verify(preguntasRepository,atMostOnce()).getPreguntas(1L);
    }

    static class MyArgsMatchers implements ArgumentMatcher<Long>{

        @Override
        public String toString() {
            return "ERROR: El argumento debe ser positivo";
        }

        @Override
        public boolean matches(Long argument) {
            return argument != null && argument > 0L;
        }
    }
}