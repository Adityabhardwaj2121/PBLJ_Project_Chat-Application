package com.chatapp.servlet;

import com.chatapp.dao.UserDAO;
import com.chatapp.dao.MessageDAO;
import com.chatapp.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private MessageDAO messageDAO = new MessageDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get all users for admin panel
        List<User> allUsers = userDAO.getAllUsers();
        request.setAttribute("users", allUsers);
        request.setAttribute("currentUser", currentUser);
        
        // Get statistics
        int totalUsers = allUsers.size();
        int onlineUsers = (int) allUsers.stream().filter(u -> "Online".equals(u.getStatus())).count();
        
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("onlineUsers", onlineUsers);
        
        request.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !currentUser.isAdmin()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String action = request.getParameter("action");
        
        switch (action) {
            case "updateUserStatus":
                handleUpdateUserStatus(request, response);
                break;
            case "deleteUser":
                handleDeleteUser(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void handleUpdateUserStatus(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String userIdStr = request.getParameter("userId");
        String newStatus = request.getParameter("status");
        
        if (userIdStr != null && newStatus != null) {
            int userId = Integer.parseInt(userIdStr);
            boolean success = userDAO.updateUserStatus(userId, newStatus);
            
            if (success) {
                response.getWriter().write("{\"success\":true}");
            } else {
                response.getWriter().write("{\"success\":false,\"error\":\"Failed to update status\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // Implementation for user deletion would go here
        // For security reasons, this is left as a placeholder
        response.getWriter().write("{\"success\":false,\"error\":\"User deletion not implemented for security\"}");
    }
}