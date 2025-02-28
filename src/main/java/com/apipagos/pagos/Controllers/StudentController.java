package com.apipagos.pagos.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.apipagos.pagos.Entities.Student;
import com.apipagos.pagos.Repositories.StudentRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;
    
    @GetMapping("/estudiantes")
    public List<Student> studentsList() {
        return studentRepo.findAll();
    }

    @GetMapping("/estudiantes/{codigo}")
    public Student getStudentByCode(@PathVariable String codigo) {
        return studentRepo.findByCodigo(codigo);
    }

    @GetMapping("/estudiantes-por-programa")
    public List<Student> StudentsByProgram(@RequestParam String programa_id) {
        return studentRepo.findByProgramId(programa_id);
    } 
}
