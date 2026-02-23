
package com.oceanview.service.impl;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.impl.UserDAOImpl;
import com.oceanview.dto.UserDTO;
import com.oceanview.entity.User;
import com.oceanview.mapper.UserMapper;
import com.oceanview.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;

public class AuthServiceImpl implements AuthService {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public UserDTO login(String username, String password) {
        User user = userDAO.login(username);
        if (user == null || !user.isActive() || user.isBlocked()) return null;

        boolean match = BCrypt.checkpw(password, user.getPasswordHash());
        if (!match) return null;

        return UserMapper.toDTO(user);  // convert entity to DTO
    }
}
