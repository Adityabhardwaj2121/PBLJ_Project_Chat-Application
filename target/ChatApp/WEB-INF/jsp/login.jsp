<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat App - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <div class="auth-form">
            <h2>Login to Chat App</h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            
            <form method="post" action="${pageContext.request.contextPath}/login">
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                
                <button type="submit" class="btn btn-primary">Login</button>
            </form>
            
            <p class="auth-link">
                Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a>
            </p>
        </div>
    </div>
</body>
</html>