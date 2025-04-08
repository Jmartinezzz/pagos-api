package com.apipagos.pagos.Services;

import com.apipagos.pagos.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apipagos.pagos.Repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return jwtService.generateToke(user.getEmail());
    }

    public String login(User userInput) {
        User user = userRepository.findByEmail(userInput.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(userInput.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales invalidas");
        }

        return jwtService.generateToke(user.getEmail());
    }
   
}
