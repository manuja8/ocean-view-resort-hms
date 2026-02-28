package com.oceanview.service;

import com.oceanview.dto.UserDTO;

import java.util.List;

public interface UserManagementService {

    void createUser(UserDTO dto, String rawPassword);

    void updateUser(UserDTO dto);

    void deleteUser(int userId);

    List<UserDTO> getAllUsers();

    List<UserDTO> searchUsers(String keyword);
}