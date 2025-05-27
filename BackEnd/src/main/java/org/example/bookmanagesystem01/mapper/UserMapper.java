package org.example.bookmanagesystem01.mapper;

import org.example.bookmanagesystem01.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface UserMapper {
    User getUserId(int id);
    User getUserByNo(String no);
    List<User> getUserByName(String name);
    List<User> getUserByPhone(String phone);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
    Optional<User> findByNoAndPassword(Map<String, Object> params);

}