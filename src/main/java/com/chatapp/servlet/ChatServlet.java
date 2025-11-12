package com.chatapp.servlet;

import com.chatapp.dao.UserDAO;
import com.chatapp.dao.GroupDAO;
import com.chatapp.model.User;
import com.chatapp.model.Group;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private GroupDAO groupDAO = new GroupDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get all users for contact list
        List<User> allUsers = userDAO.getAllUsers();
        allUsers.removeIf(user -> user.getUserId() == currentUser.getUserId());
        
        // Get user's groups
        List<Group> userGroups = groupDAO.getUserGroups(currentUser.getUserId());
        
        request.setAttribute("users", allUsers);
        request.setAttribute("groups", userGroups);
        request.setAttribute("currentUser", currentUser);
        
        request.getRequestDispatcher("/WEB-INF/jsp/chat.jsp").forward(request, response);
    }
}