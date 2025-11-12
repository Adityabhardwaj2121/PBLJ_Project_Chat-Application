package com.chatapp.servlet;

import com.chatapp.dao.UserDAO;
import com.chatapp.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation
        if (username == null || email == null || password == null || confirmPassword == null ||
            username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Check if username already exists
        if (userDAO.isUsernameExists(username.trim())) {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Create new user
        User user = new User(username.trim(), email.trim(), password);
        
        if (userDAO.registerUser(user)) {
            request.setAttribute("success", "Registration successful! Please login.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }
}