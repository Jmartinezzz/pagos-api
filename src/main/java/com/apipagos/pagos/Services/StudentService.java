package com.apipagos.pagos.Services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apipagos.pagos.Entities.Student;
import com.apipagos.pagos.Repositories.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepo;
    
    public Student save(Student student){
        String id = UUID.randomUUID().toString();
        student.setId(id);
        return studentRepo.save(student);
    }
}
