package com.apipagos.pagos.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apipagos.pagos.Entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Student findByCodigo(String codigo);
    List<Student> findByProgramId(String programId);
    
}
