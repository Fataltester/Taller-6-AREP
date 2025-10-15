/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arep.concurrencia.Controller;

import edu.eci.arep.concurrencia.Model.User;
import edu.eci.arep.concurrencia.Model.dto.UserDto;
import edu.eci.arep.concurrencia.Repository.UserRepository;
import edu.eci.arep.concurrencia.Service.AuthService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto user) {
        authService.registerUser(user.getEmail(), user.getPassword());
        return ResponseEntity.ok("Registro Exitoso");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto user) {
        Optional<User> loggedUser = authService.loginUser(user.getEmail(), user.getPassword());
        if (loggedUser.isPresent()) {
            return ResponseEntity.ok("Login Exitoso");
        } else {
            return ResponseEntity.status(401).body("Credenciales NO válidas");
        }
    }
}
