package com.chatapp.dao;

import com.chatapp.model.Message;
import com.chatapp.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public boolean sendMessage(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, group_id, content, message_type, file_name, timestamp, is_read) VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, message.getSenderId());
            stmt.setObject(2, message.getReceiverId() > 0 ? message.getReceiverId() : null);
            stmt.setObject(3, message.getGroupId() > 0 ? message.getGroupId() : null);
            stmt.setString(4, message.getContent());
            stmt.setString(5, message.getMessageType());
            stmt.setString(6, message.getFileName());
            stmt.setBoolean(7, message.isRead());
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    message.setMessageId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Message> getPrivateMessages(int userId1, int userId2) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE " +
                    "((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) " +
                    "AND group_id IS NULL ORDER BY timestamp";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId1);
            stmt.setInt(2, userId2);
            stmt.setInt(3, userId2);
            stmt.setInt(4, userId1);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setContent(rs.getString("content"));
                message.setMessageType(rs.getString("message_type"));
                message.setFileName(rs.getString("file_name"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    public List<Message> getGroupMessages(int groupId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE group_id = ? ORDER BY timestamp";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, groupId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setGroupId(rs.getInt("group_id"));
                message.setContent(rs.getString("content"));
                message.setMessageType(rs.getString("message_type"));
                message.setFileName(rs.getString("file_name"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    public boolean markMessageAsRead(int messageId) {
        String sql = "UPDATE messages SET is_read = true WHERE message_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, messageId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Message> searchMessages(int userId, String searchTerm) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE " +
                    "(sender_id = ? OR receiver_id = ?) AND content LIKE ? " +
                    "ORDER BY timestamp DESC LIMIT 50";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setString(3, "%" + searchTerm + "%");
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("message_id"));
                message.setSenderId(rs.getInt("sender_id"));
                message.setReceiverId(rs.getInt("receiver_id"));
                message.setGroupId(rs.getInt("group_id"));
                message.setContent(rs.getString("content"));
                message.setMessageType(rs.getString("message_type"));
                message.setTimestamp(rs.getTimestamp("timestamp"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
    public int getUnreadMessageCount(int userId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND is_read = false";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}