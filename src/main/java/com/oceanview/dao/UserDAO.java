package com.oceanview.dao;

import com.oceanview.entity.User;

import java.util.List;

public interface UserDAO {
    User login(String username);

    void updateLastLogin(int userId);

    int save(User user);

    void update(User user);

    void delete(int userId);

    User findById(int userId);

    List<User> findAll();

    List<User> search(String keyword);
}
