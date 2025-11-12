<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat App - Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <div class="auth-form">
            <h2>Register for Chat App</h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            
            <form method="post" action="${pageContext.request.contextPath}/register">
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>
                </div>
                
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required minlength="6">
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                </div>
                
                <button type="submit" class="btn btn-primary">Register</button>
            </form>
            
            <p class="auth-link">
                Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a>
            </p>
        </div>
    </div>
</body>
</html>