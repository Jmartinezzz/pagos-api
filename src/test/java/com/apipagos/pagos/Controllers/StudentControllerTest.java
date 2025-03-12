package com.apipagos.pagos.Controllers;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.hamcrest.Matchers.nullValue;
import org.springframework.http.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.apipagos.pagos.Entities.Student;
import com.apipagos.pagos.Repositories.StudentRepository;
import com.apipagos.pagos.Services.StudentService;

// @WebMvcTest(StudentController.class)
@SpringBootTest
public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentRepository studentRepo;
    
    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private List<Student> studentList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build(); // Prepara el MockMvc con el controlador

        // algunos estudiantes
        studentList = Arrays.asList(
                new Student(
                    "cod1",
                    "Juan",
                    "camero",
                    "123",
                    "aws",
                    null
                ), 
                new Student(
                    "cod2", 
                    "Maria",
                    "gaviria",
                    "124",
                    "aws2", 
                    null
                ),
                new Student(
                    "cod3",
                    "Pedro",
                    "Perez",
                    "125",
                    "aws",
                    null
                )
            );
    }

    @Test
    @DisplayName("students list test")
    public void testStudentsList() throws Exception {    
        when(studentRepo.findAll()).thenReturn(studentList); // Simula la respuesta del repositorio

        mockMvc.perform(get("/api/estudiantes"))
            .andExpect(status().isOk()) // Verificamos que el estado HTTP sea 200
            .andExpect(jsonPath("$[0].id").value("cod1"))
            .andExpect(jsonPath("$[0].nombre").value("Juan"))
            .andExpect(jsonPath("$[0].apellido").value("camero"))
            .andExpect(jsonPath("$[0].codigo").value("123"))
            .andExpect(jsonPath("$[0].programId").value("aws"))
            .andExpect(jsonPath("$[0].foto").value(nullValue()))
            .andExpect(jsonPath("$[1].id").value("cod2"))
            .andExpect(jsonPath("$[1].codigo").value("124"))
            .andExpect(jsonPath("$[1].nombre").value("Maria"))
            .andExpect(jsonPath("$[1].apellido").value("gaviria"))
            .andExpect(jsonPath("$[1].programId").value("aws2"))
            .andExpect(jsonPath("$[1].foto").value(nullValue()));

        verify(studentRepo, times(1)).findAll();
    }

    @Test
    @DisplayName("get a student by his code")
    public void testGetStudentByCode() throws Exception {
        Student student = new Student(
                "123asder", 
                "Juan", 
                "camero",
                "123",
                "aws",
                null
            );
        when(studentRepo.findByCodigo("123asder"))
            .thenReturn(student);

        mockMvc.perform(get("/api/estudiantes/123asder"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.apellido").value("camero"))
            .andExpect(jsonPath("$.nombre").value("Juan"));
        
        verify(studentRepo, times(1)).findByCodigo("123asder");
    }

    @Test
    public void testStudentsByProgramFound() throws Exception {
        // Simulamos la respuesta del repositorio
        when(studentRepo.findByProgramId("aws2")).thenReturn(
            studentList.stream()
                .filter(student -> "aws2".equals(student.getProgramId()))
                .collect(Collectors.toList())
        );

        mockMvc.perform(get("/api/estudiantes/estudiantes-por-programa")
                .param("programa_id", "aws2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("cod2"))
            .andExpect(jsonPath("$[0].nombre").value("Maria"))
            .andExpect(jsonPath("$[0].codigo").value("124"))
            .andExpect(jsonPath("$[0].apellido").value("gaviria"));

        // Verificar que el método findByProgramId fue llamado con el programa_id correcto
        verify(studentRepo, times(1)).findByProgramId("aws2");
    }

    @Test
    public void testStudentsByProgramNotFound() throws Exception {
        // Simulamos que no se encuentran estudiantes para el programa
        when(studentRepo.findByProgramId("aws23")).thenReturn(
            studentList.stream()
                .filter(student -> "aws23".equals(student.getProgramId()))
                .collect(Collectors.toList())
        );

        mockMvc.perform(get("/api/estudiantes/estudiantes-por-programa")
                .param("programa_id", "aws23"))
            .andExpect(status().isOk()) // El status sigue siendo OK porque la lista vacía es válida
            .andExpect(content().json("[]")); // Esperamos que la respuesta sea una lista vacía

        // Verificar que el método findByProgramId fue llamado con el programa_id correcto
        verify(studentRepo, times(1)).findByProgramId("aws23");
    }

    @Test
    public void testSaveStudentSuccess() throws Exception {
        Student validStudent = new Student("cod5", "Ernest", "galindo", "128", "aws", null);

        when(studentService.save(any(Student.class))).thenReturn(validStudent);

        mockMvc.perform(post("/api/estudiantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"cod5\",\"nombre\":\"Ernest\",\"apellido\":\"galindo\",\"codigo\":\"128\",\"programId\":\"aws\"}"))
            .andExpect(status().isCreated()) // Expect HTTP 201
            .andExpect(jsonPath("$.id").value("cod5"))
            .andExpect(jsonPath("$.nombre").value("Ernest"))
            .andExpect(jsonPath("$.apellido").value("galindo"))
            .andExpect(jsonPath("$.codigo").value("128"))
            .andExpect(jsonPath("$.programId").value("aws"))
            .andDo(print());
    }

    @Test
    public void testSaveStudentFailureMissingNombre() throws Exception {
        String invalidStudentJson = "{\"id\":\"cod10\",\"apellido\":\"Camero\",\"codigo\":\"113\",\"programId\":\"aws\"}";

        mockMvc.perform(post("/api/estudiantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidStudentJson))
            .andExpect(status().isBadRequest()) // Expect HTTP 400
            .andExpect(jsonPath("$.nombre").value("El nombre es obligatorio"))
            .andDo(print());
    }

}
