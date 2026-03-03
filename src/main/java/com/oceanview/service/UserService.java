package com.oceanview.service;

import com.oceanview.entity.User;

import java.util.List;

public interface UserService {

    List<User> list(String q);

    User getById(int id);

    void create(User user, int adminUserId, String plainPassword);

    void update(User user, int adminUserId, String plainPasswordOrNull);

    void delete(int id);

    String friendlyMessage(Exception ex);
}