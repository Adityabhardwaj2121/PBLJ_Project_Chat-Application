<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Not Found - Chat App</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <div class="auth-form">
            <h2>404 - Page Not Found</h2>
            <p>The page you are looking for does not exist.</p>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Go to Login</a>
        </div>
    </div>
</body>
</html>