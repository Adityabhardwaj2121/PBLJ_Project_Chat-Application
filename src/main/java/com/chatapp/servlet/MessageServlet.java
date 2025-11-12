package com.chatapp.servlet;

import com.chatapp.dao.MessageDAO;
import com.chatapp.dao.UserDAO;
import com.chatapp.model.Message;
import com.chatapp.model.User;
import com.chatapp.util.XMLMessageLogger;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/message")
public class MessageServlet extends HttpServlet {
    private MessageDAO messageDAO = new MessageDAO();
    private UserDAO userDAO = new UserDAO();
    private Gson gson = new Gson();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        String action = request.getParameter("action");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            switch (action) {
                case "send":
                    handleSendMessage(request, response, currentUser, out);
                    break;
                case "getMessages":
                    handleGetMessages(request, response, currentUser, out);
                    break;
                case "markRead":
                    handleMarkRead(request, response, out);
                    break;
                case "search":
                    handleSearchMessages(request, response, currentUser, out);
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"error\":\"Invalid action\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"Internal server error\"}");
        }
    }
    
    private void handleSendMessage(HttpServletRequest request, HttpServletResponse response, 
                                 User currentUser, PrintWriter out) throws IOException {
        String content = request.getParameter("content");
        String receiverIdStr = request.getParameter("receiverId");
        String groupIdStr = request.getParameter("groupId");
        String messageType = request.getParameter("messageType");
        
        if (content == null || content.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Message content is required\"}");
            return;
        }
        
        Message message = new Message();
        message.setSenderId(currentUser.getUserId());
        message.setContent(content.trim());
        message.setMessageType(messageType != null ? messageType : "text");
        
        if (receiverIdStr != null && !receiverIdStr.isEmpty()) {
            message.setReceiverId(Integer.parseInt(receiverIdStr));
        }
        
        if (groupIdStr != null && !groupIdStr.isEmpty()) {
            message.setGroupId(Integer.parseInt(groupIdStr));
        }
        
        if (messageDAO.sendMessage(message)) {
            // Log to XML if it's a private message
            if (message.getReceiverId() > 0) {
                User receiver = userDAO.getUserById(message.getReceiverId());
                XMLMessageLogger.logMessage(message, currentUser.getUsername(), 
                                          receiver != null ? receiver.getUsername() : "Unknown");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("messageId", message.getMessageId());
            out.write(gson.toJson(result));
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"Failed to send message\"}");
        }
    }
    
    private void handleGetMessages(HttpServletRequest request, HttpServletResponse response, 
                                 User currentUser, PrintWriter out) throws IOException {
        String receiverIdStr = request.getParameter("receiverId");
        String groupIdStr = request.getParameter("groupId");
        
        List<Message> messages;
        
        if (receiverIdStr != null && !receiverIdStr.isEmpty()) {
            int receiverId = Integer.parseInt(receiverIdStr);
            messages = messageDAO.getPrivateMessages(currentUser.getUserId(), receiverId);
        } else if (groupIdStr != null && !groupIdStr.isEmpty()) {
            int groupId = Integer.parseInt(groupIdStr);
            messages = messageDAO.getGroupMessages(groupId);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Receiver ID or Group ID is required\"}");
            return;
        }
        
        // Create enhanced message objects with sender usernames for group chats
        List<Map<String, Object>> enhancedMessages = new ArrayList<>();
        for (Message message : messages) {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("messageId", message.getMessageId());
            messageMap.put("senderId", message.getSenderId());
            messageMap.put("receiverId", message.getReceiverId());
            messageMap.put("groupId", message.getGroupId());
            messageMap.put("content", message.getContent());
            messageMap.put("messageType", message.getMessageType());
            messageMap.put("timestamp", message.getTimestamp());
            messageMap.put("isRead", message.isRead());
            
            // Add sender username for group messages
            if (groupIdStr != null && !groupIdStr.isEmpty()) {
                User sender = userDAO.getUserById(message.getSenderId());
                messageMap.put("senderUsername", sender != null ? sender.getUsername() : "Unknown User");
            }
            
            enhancedMessages.add(messageMap);
        }
        
        out.write(gson.toJson(enhancedMessages));
    }
    
    private void handleMarkRead(HttpServletRequest request, HttpServletResponse response, 
                              PrintWriter out) throws IOException {
        String messageIdStr = request.getParameter("messageId");
        
        if (messageIdStr == null || messageIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Message ID is required\"}");
            return;
        }
        
        int messageId = Integer.parseInt(messageIdStr);
        boolean success = messageDAO.markMessageAsRead(messageId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        out.write(gson.toJson(result));
    }
    
    private void handleSearchMessages(HttpServletRequest request, HttpServletResponse response, 
                                    User currentUser, PrintWriter out) throws IOException {
        String searchTerm = request.getParameter("searchTerm");
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Search term is required\"}");
            return;
        }
        
        List<Message> messages = messageDAO.searchMessages(currentUser.getUserId(), searchTerm.trim());
        out.write(gson.toJson(messages));
    }
}