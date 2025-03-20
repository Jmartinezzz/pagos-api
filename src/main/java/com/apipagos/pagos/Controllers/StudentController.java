package com.apipagos.pagos.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.apipagos.pagos.Entities.Student;
import com.apipagos.pagos.Repositories.StudentRepository;
import com.apipagos.pagos.Services.StudentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin("*")
@RequestMapping("api/estudiantes")
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StudentService studentService;

    @GetMapping()
    public List<Student> studentsList() {
        return studentRepo.findAll();
    }

    @GetMapping("/paginados")
    public Page<Student> studentsListPaginated(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepo.findAll(pageable);
    }

    @PostMapping()
    public ResponseEntity<?> save(@Valid @RequestBody Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(studentService.save(student), HttpStatus.CREATED);
    }

    @GetMapping("{codigo}")
    public Student getStudentByCode(@PathVariable String codigo) {
        return studentRepo.findByCodigo(codigo);
    }

    @GetMapping("/estudiantes-por-programa")
    public List<Student> StudentsByProgram(@RequestParam String programa_id) {
        return studentRepo.findByProgramId(programa_id);
    }
}
