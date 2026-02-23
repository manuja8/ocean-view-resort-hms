
package com.oceanview.service;

import com.oceanview.dto.UserDTO;

public interface AuthService {
    UserDTO login(String username, String password);
}
