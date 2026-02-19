
package com.oceanview.mapper;

import com.oceanview.entity.User;
import com.oceanview.dto.UserDTO;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRoleName());
        return dto;
    }
}
