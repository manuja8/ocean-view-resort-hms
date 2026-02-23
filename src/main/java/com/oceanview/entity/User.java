package com.oceanview.entity;

public abstract class User {

    protected int userId;
    protected String username;
    protected String passwordHash;
    protected String roleName;
    protected boolean isActive;
    protected boolean isBlocked;

    public User() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    //verify password
    public boolean verifyPassword(String pw) {
        return org.mindrot.jbcrypt.BCrypt.checkpw(pw, this.passwordHash);
    }
}
