package com.oceanview.service.impl;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.impl.UserDAOImpl;
import com.oceanview.dto.UserDTO;
import com.oceanview.entity.User;
import com.oceanview.factory.UserFactory;
import com.oceanview.mapper.UserMapper;
import com.oceanview.service.UserManagementService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.stream.Collectors;

public class UserManagementServiceImpl implements UserManagementService {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public void createUser(UserDTO dto, String rawPassword) {

        User user = UserFactory.createUser(dto.getRole());

        user.setUsername(dto.getUsername());
        user.setRoleName(dto.getRole());
        user.setPasswordHash(BCrypt.hashpw(rawPassword, BCrypt.gensalt()));

        userDAO.save(user);
    }

    @Override
    public void updateUser(UserDTO dto) {
        User user = userDAO.findById(dto.getUserId());
        user.setUsername(dto.getUsername());
        userDAO.update(user);
    }

    @Override
    public void deleteUser(int userId) {
        userDAO.delete(userId);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userDAO.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> searchUsers(String keyword) {
        return userDAO.search(keyword)
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}