package com.chatapp.model;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private int senderId;
    private int receiverId;
    private int groupId;
    private String content;
    private String messageType; // text, image, file
    private String fileName;
    private Timestamp timestamp;
    private boolean isRead;
    
    // Constructors
    public Message() {}
    
    public Message(int senderId, int receiverId, String content, String messageType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.messageType = messageType;
        this.isRead = false;
    }
    
    public Message(int senderId, int groupId, String content, String messageType, boolean isGroupMessage) {
        this.senderId = senderId;
        this.groupId = groupId;
        this.content = content;
        this.messageType = messageType;
        this.isRead = false;
    }
    
    // Getters and Setters
    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }
    
    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }
    
    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }
    
    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}