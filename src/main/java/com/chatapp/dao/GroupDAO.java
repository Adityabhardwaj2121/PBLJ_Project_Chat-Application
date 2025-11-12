package com.chatapp.dao;

import com.chatapp.model.Group;
import com.chatapp.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {
    
    public boolean createGroup(Group group) {
        String sql = "INSERT INTO groups (group_name, description, created_by, created_at) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, group.getGroupName());
            stmt.setString(2, group.getDescription());
            stmt.setInt(3, group.getCreatedBy());
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int groupId = generatedKeys.getInt(1);
                    group.setGroupId(groupId);
                    
                    // Add creator as first member
                    addGroupMember(groupId, group.getCreatedBy());
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean addGroupMember(int groupId, int userId) {
        String sql = "INSERT INTO group_members (group_id, user_id, joined_at) VALUES (?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, groupId);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean removeGroupMember(int groupId, int userId) {
        String sql = "DELETE FROM group_members WHERE group_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, groupId);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Group> getUserGroups(int userId) {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT g.* FROM groups g " +
                    "INNER JOIN group_members gm ON g.group_id = gm.group_id " +
                    "WHERE gm.user_id = ? ORDER BY g.group_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Group group = new Group();
                group.setGroupId(rs.getInt("group_id"));
                group.setGroupName(rs.getString("group_name"));
                group.setDescription(rs.getString("description"));
                group.setCreatedBy(rs.getInt("created_by"));
                group.setCreatedAt(rs.getTimestamp("created_at"));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }
    
    public Group getGroupById(int groupId) {
        String sql = "SELECT * FROM groups WHERE group_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, groupId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Group group = new Group();
                group.setGroupId(rs.getInt("group_id"));
                group.setGroupName(rs.getString("group_name"));
                group.setDescription(rs.getString("description"));
                group.setCreatedBy(rs.getInt("created_by"));
                group.setCreatedAt(rs.getTimestamp("created_at"));
                
                // Get member IDs
                group.setMemberIds(getGroupMemberIds(groupId));
                return group;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Integer> getGroupMemberIds(int groupId) {
        List<Integer> memberIds = new ArrayList<>();
        String sql = "SELECT user_id FROM group_members WHERE group_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, groupId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                memberIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberIds;
    }
    
    public boolean isUserInGroup(int groupId, int userId) {
        String sql = "SELECT COUNT(*) FROM group_members WHERE group_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, groupId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Group> searchGroups(String searchTerm) {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM groups WHERE group_name LIKE ? OR description LIKE ? ORDER BY group_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Group group = new Group();
                group.setGroupId(rs.getInt("group_id"));
                group.setGroupName(rs.getString("group_name"));
                group.setDescription(rs.getString("description"));
                group.setCreatedBy(rs.getInt("created_by"));
                group.setCreatedAt(rs.getTimestamp("created_at"));
                groups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }
}