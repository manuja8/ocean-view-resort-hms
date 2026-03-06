package com.oceanview.service.impl;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.UserRoleDAO;
import com.oceanview.dao.impl.UserDAOImpl;
import com.oceanview.dao.impl.UserRoleDAOImpl;
import com.oceanview.dto.UserRoleDTO;
import com.oceanview.entity.User;
import com.oceanview.factory.UserFactory;
import com.oceanview.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO = new UserDAOImpl();
    private final UserRoleDAO roleDAO = new UserRoleDAOImpl();

    @Override
    public List<User> list(String q) {
        return (q == null || q.trim().isEmpty()) ? userDAO.findAll() : userDAO.search(q.trim());
    }

    @Override
    public User getById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public void create(User user, int adminUserId, String plainPassword) {

        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required to create a user.");
        }

        normalizeAndValidate(user);

        user.setCreatedByUserId(adminUserId);
        user.setPasswordHash(BCrypt.hashpw(plainPassword.trim(), BCrypt.gensalt()));

        userDAO.save(user);
    }

    @Override
    public void update(User user, int adminUserId, String plainPasswordOrNull) {

        normalizeAndValidate(user);

        user.setUpdatedByUserId(adminUserId);


        if (plainPasswordOrNull != null && !plainPasswordOrNull.trim().isEmpty()) {
            user.setPasswordHash(BCrypt.hashpw(plainPasswordOrNull.trim(), BCrypt.gensalt()));
        } else {
            user.setPasswordHash(null);
        }

        userDAO.update(user);
    }

    @Override
    public void delete(int id) {
        userDAO.delete(id);
    }

    private void normalizeAndValidate(User user) {

        if (user == null) throw new IllegalArgumentException("Invalid user.");
        if (user.getUsername() == null || user.getUsername().trim().isEmpty())
            throw new IllegalArgumentException("Username is required.");
        if (user.getFullName() == null || user.getFullName().trim().isEmpty())
            throw new IllegalArgumentException("Full name is required.");
        if (user.getAddress() == null || user.getAddress().trim().isEmpty())
            throw new IllegalArgumentException("Address is required.");
        if (user.getContactNo() == null || user.getContactNo().trim().isEmpty())
            throw new IllegalArgumentException("Contact number is required.");


        UserRoleDTO role = roleDAO.findById(user.getRoleId());
        if (role == null) throw new IllegalArgumentException("Invalid role.");


        User typed = UserFactory.createUser(role.getRoleName());
        if (typed == null) throw new IllegalArgumentException("Invalid role.");


        typed.setUserId(user.getUserId());
        typed.setUsername(user.getUsername().trim());
        typed.setFullName(user.getFullName().trim());
        typed.setAddress(user.getAddress().trim());
        typed.setContactNo(user.getContactNo().trim());

        typed.setRoleId(user.getRoleId());
        typed.setRoleName(role.getRoleName());

        typed.setActive(user.isActive());
        typed.setBlocked(user.isBlocked());

        // expiry
        if (user.getExpiryDate() == null) {
            typed.setExpiryDate(LocalDateTime.now().plusYears(1));
        } else {
            typed.setExpiryDate(user.getExpiryDate());
        }

        user.setUsername(typed.getUsername());
        user.setFullName(typed.getFullName());
        user.setAddress(typed.getAddress());
        user.setContactNo(typed.getContactNo());
        user.setRoleName(typed.getRoleName());
        user.setExpiryDate(typed.getExpiryDate());
    }

    @Override
    public String friendlyMessage(Exception ex) {
        Throwable t = ex;
        while (t.getCause() != null) t = t.getCause();

        String msg = t.getMessage() == null ? ex.getMessage() : t.getMessage();
        if (msg == null) return "Operation failed.";

        if (msg.contains("Duplicate entry") || msg.contains("1062")) {
            String lower = msg.toLowerCase();
            if (lower.contains("username")) return "Username already exists. Please choose another one.";
            if (lower.contains("contact_no") || lower.contains("contact"))
                return "Contact number already exists. Please use another.";
            return "Duplicate value detected. Please check again.";
        }

        return msg;
    }
}