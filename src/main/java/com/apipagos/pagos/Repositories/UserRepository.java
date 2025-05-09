package com.apipagos.pagos.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apipagos.pagos.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
