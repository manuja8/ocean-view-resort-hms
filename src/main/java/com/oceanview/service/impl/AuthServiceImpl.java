package com.oceanview.service.impl;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.impl.UserDAOImpl;
import com.oceanview.entity.User;
import com.oceanview.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;

public class AuthServiceImpl implements AuthService {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public User login(String username, String password) {

        User user = userDAO.login(username);

        if (user == null) return null;

        if (!user.isActive()) return null;

        if (user.isBlocked()) return null;

        boolean match = BCrypt.checkpw(password, user.getPasswordHash());

        if (match) {
            return user;
        }

        return null;
    }
}

