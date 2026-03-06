package com.oceanview.factory;

import com.oceanview.entity.Admin;
import com.oceanview.entity.Receptionist;
import com.oceanview.entity.User;

public class UserFactory {

    public static User createUser(String role) {
        if (role == null) return null;

        String r = role.trim().toLowerCase();
        switch (r) {
            case "admin":
                return new Admin();
            case "receptionist":
                return new Receptionist();
            default:
                return null;
        }
    }
}