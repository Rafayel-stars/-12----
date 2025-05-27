package org.example.bookmanagesystem01.service;

import org.example.bookmanagesystem01.entity.User;
import org.example.bookmanagesystem01.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 移除 static 修饰符
    public boolean saveUser(User user) {
        if (userRepository.existsByNo(user.getNo())) {
            return false;
        }
        userRepository.save(user);
        return true;
    }
}