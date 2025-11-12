package com.chatapp.filter;

import com.chatapp.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/chat", "/message", "/group", "/admin/*"})
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        
        if (user == null) {
            // User is not logged in
            String requestURI = httpRequest.getRequestURI();
            String contextPath = httpRequest.getContextPath();
            
            if (requestURI.startsWith(contextPath + "/message") || 
                requestURI.startsWith(contextPath + "/group")) {
                // For AJAX requests, return 401 status
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("{\"error\":\"Authentication required\"}");
                return;
            } else {
                // For regular requests, redirect to login
                httpResponse.sendRedirect(contextPath + "/login");
                return;
            }
        }
        
        // User is authenticated, continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}