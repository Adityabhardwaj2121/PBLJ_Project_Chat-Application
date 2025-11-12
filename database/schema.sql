-- Chat Application Database Schema
-- Create database
CREATE DATABASE IF NOT EXISTS chatapp_db;
USE chatapp_db;

-- Users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    status ENUM('Online', 'Offline', 'Busy') DEFAULT 'Offline',
    last_seen TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_admin BOOLEAN DEFAULT FALSE,
    INDEX idx_username (username),
    INDEX idx_status (status)
);

-- Groups table
CREATE TABLE groups (
    group_id INT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_group_name (group_name)
);

-- Group members table
CREATE TABLE group_members (
    group_id INT NOT NULL,
    user_id INT NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (group_id, user_id),
    FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Messages table
CREATE TABLE messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NULL, -- NULL for group messages
    group_id INT NULL, -- NULL for private messages
    content TEXT NOT NULL,
    message_type ENUM('text', 'image', 'file') DEFAULT 'text',
    file_name VARCHAR(255) NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE CASCADE,
    INDEX idx_sender (sender_id),
    INDEX idx_receiver (receiver_id),
    INDEX idx_group (group_id),
    INDEX idx_timestamp (timestamp),
    INDEX idx_is_read (is_read)
);

-- File uploads table (for file sharing feature)
CREATE TABLE file_uploads (
    file_id INT AUTO_INCREMENT PRIMARY KEY,
    message_id INT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    upload_path VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (message_id) REFERENCES messages(message_id) ON DELETE CASCADE
);

-- User sessions table (for tracking online users)
CREATE TABLE user_sessions (
    session_id VARCHAR(255) PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_last_activity (last_activity)
);

-- Notifications table
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    type ENUM('message', 'group_invite', 'friend_request') NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
);

-- Insert sample admin user (password: admin123)
INSERT INTO users (username, email, password, is_admin, status) 
VALUES ('admin', 'admin@chatapp.com', 'admin123', TRUE, 'Offline');

-- Insert sample users for testing (password: user123)
INSERT INTO users (username, email, password, status) VALUES 
('john_doe', 'john@example.com', 'user123', 'Offline'),
('jane_smith', 'jane@example.com', 'user123', 'Offline'),
('bob_wilson', 'bob@example.com', 'user123', 'Offline'),
('alice_brown', 'alice@example.com', 'user123', 'Offline');

-- Insert sample group
INSERT INTO groups (group_name, description, created_by) 
VALUES ('General Discussion', 'A place for general conversations', 1);

-- Add users to the sample group
INSERT INTO group_members (group_id, user_id) VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);

-- Insert sample messages
INSERT INTO messages (sender_id, receiver_id, content, message_type) VALUES 
(2, 3, 'Hello Jane! How are you doing?', 'text'),
(3, 2, 'Hi John! I am doing great, thanks for asking!', 'text'),
(2, 3, 'That is wonderful to hear!', 'text');

INSERT INTO messages (sender_id, group_id, content, message_type) VALUES 
(1, 1, 'Welcome everyone to the General Discussion group!', 'text'),
(2, 1, 'Thanks for creating this group!', 'text'),
(3, 1, 'Looking forward to great discussions!', 'text');