package com.oceanview.dao;

import com.oceanview.dto.UserRoleDTO;

import java.util.List;

public interface UserRoleDAO {
    List<UserRoleDTO> findAll();

    UserRoleDTO findById(int roleId);
}