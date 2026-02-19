package com.oceanview.dao;

import com.oceanview.entity.User;

public interface UserDAO {
    User login(String username);
}
