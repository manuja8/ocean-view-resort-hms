package com.oceanview.factory;

import com.oceanview.entity.Admin;
import com.oceanview.entity.Receptionist;
import com.oceanview.entity.User;

public class UserFactory {

    public static User createUser(String role) {

        if (role == null) return null;

        switch (role.toLowerCase()) {
            case "admin":
                return new Admin();

            case "receptionist":
            case "user":
                return new Receptionist();

            default:
                return null;
        }
    }
}
