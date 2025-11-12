<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Server Error - Chat App</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-body">
    <div class="auth-container">
        <div class="auth-form">
            <h2>500 - Server Error</h2>
            <p>An internal server error occurred. Please try again later.</p>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Go to Login</a>
        </div>
    </div>
</body>
</html>