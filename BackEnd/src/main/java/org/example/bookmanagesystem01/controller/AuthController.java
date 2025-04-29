package org.example.bookmanagesystem01.controller;

import org.example.bookmanagesystem01.entity.User;
import org.example.bookmanagesystem01.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String no, @RequestParam String password) {
        Optional<User> userOptional = userRepository.findByNoAndPassword(no, password);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.badRequest().body("用户名或密码错误");
        }
    }
}