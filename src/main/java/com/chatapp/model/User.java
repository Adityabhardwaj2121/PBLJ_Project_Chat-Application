package com.chatapp.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String status; // Online, Offline, Busy
    private Timestamp lastSeen;
    private Timestamp createdAt;
    private boolean isAdmin;
    
    // Constructors
    public User() {}
    
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = "Offline";
        this.isAdmin = false;
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getLastSeen() { return lastSeen; }
    public void setLastSeen(Timestamp lastSeen) { this.lastSeen = lastSeen; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
}