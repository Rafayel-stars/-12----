package org.example.bookmanagesystem01.controller;

import org.example.bookmanagesystem01.entity.User;
import org.example.bookmanagesystem01.repository.UserRepository;
import org.example.bookmanagesystem01.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder; // 注入密码编码器

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String no,
            @RequestParam String password
    ) {
        // 1. 查询用户是否存在
        Optional<User> userOptional = userRepository.findByNo(no); // 仅通过 no 查询用户
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("用户不存在");
        }
        User user = userOptional.get();

        // 2. 验证密码（使用 BCryptPasswordEncoder）
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.badRequest().body("密码错误");
        }

        // 3. 生成 Token
        String token = jwtUtils.generateToken(user, user.getUserType());

        // 4. 返回 Token 和用户信息
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return ResponseEntity.ok(response);
    }
}