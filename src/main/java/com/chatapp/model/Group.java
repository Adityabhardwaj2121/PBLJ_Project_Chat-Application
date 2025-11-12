package com.chatapp.model;

import java.sql.Timestamp;
import java.util.List;

public class Group {
    private int groupId;
    private String groupName;
    private String description;
    private int createdBy;
    private Timestamp createdAt;
    private List<Integer> memberIds;
    
    // Constructors
    public Group() {}
    
    public Group(String groupName, String description, int createdBy) {
        this.groupName = groupName;
        this.description = description;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }
    
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public List<Integer> getMemberIds() { return memberIds; }
    public void setMemberIds(List<Integer> memberIds) { this.memberIds = memberIds; }
}