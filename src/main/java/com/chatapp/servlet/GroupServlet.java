package com.chatapp.servlet;

import com.chatapp.dao.GroupDAO;
import com.chatapp.model.Group;
import com.chatapp.model.User;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/group")
public class GroupServlet extends HttpServlet {
    private GroupDAO groupDAO = new GroupDAO();
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
                case "create":
                    handleCreateGroup(request, response, currentUser, out);
                    break;
                case "join":
                    handleJoinGroup(request, response, currentUser, out);
                    break;
                case "leave":
                    handleLeaveGroup(request, response, currentUser, out);
                    break;
                case "getMembers":
                    handleGetMembers(request, response, out);
                    break;
                case "search":
                    handleSearchGroups(request, response, out);
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
    
    private void handleCreateGroup(HttpServletRequest request, HttpServletResponse response, 
                                 User currentUser, PrintWriter out) throws IOException {
        String groupName = request.getParameter("groupName");
        String description = request.getParameter("description");
        
        if (groupName == null || groupName.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Group name is required\"}");
            return;
        }
        
        Group group = new Group(groupName.trim(), description, currentUser.getUserId());
        
        if (groupDAO.createGroup(group)) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("groupId", group.getGroupId());
            result.put("groupName", group.getGroupName());
            out.write(gson.toJson(result));
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\":\"Failed to create group\"}");
        }
    }
    
    private void handleJoinGroup(HttpServletRequest request, HttpServletResponse response, 
                               User currentUser, PrintWriter out) throws IOException {
        String groupIdStr = request.getParameter("groupId");
        
        if (groupIdStr == null || groupIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Group ID is required\"}");
            return;
        }
        
        int groupId = Integer.parseInt(groupIdStr);
        
        if (groupDAO.isUserInGroup(groupId, currentUser.getUserId())) {
            out.write("{\"error\":\"You are already a member of this group\"}");
            return;
        }
        
        boolean success = groupDAO.addGroupMember(groupId, currentUser.getUserId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        if (!success) {
            result.put("error", "Failed to join group");
        }
        out.write(gson.toJson(result));
    }
    
    private void handleLeaveGroup(HttpServletRequest request, HttpServletResponse response, 
                                User currentUser, PrintWriter out) throws IOException {
        String groupIdStr = request.getParameter("groupId");
        
        if (groupIdStr == null || groupIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Group ID is required\"}");
            return;
        }
        
        int groupId = Integer.parseInt(groupIdStr);
        boolean success = groupDAO.removeGroupMember(groupId, currentUser.getUserId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        if (!success) {
            result.put("error", "Failed to leave group");
        }
        out.write(gson.toJson(result));
    }
    
    private void handleGetMembers(HttpServletRequest request, HttpServletResponse response, 
                                PrintWriter out) throws IOException {
        String groupIdStr = request.getParameter("groupId");
        
        if (groupIdStr == null || groupIdStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Group ID is required\"}");
            return;
        }
        
        int groupId = Integer.parseInt(groupIdStr);
        List<Integer> memberIds = groupDAO.getGroupMemberIds(groupId);
        
        out.write(gson.toJson(memberIds));
    }
    
    private void handleSearchGroups(HttpServletRequest request, HttpServletResponse response, 
                                  PrintWriter out) throws IOException {
        String searchTerm = request.getParameter("searchTerm");
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\":\"Search term is required\"}");
            return;
        }
        
        List<Group> groups = groupDAO.searchGroups(searchTerm.trim());
        out.write(gson.toJson(groups));
    }
}