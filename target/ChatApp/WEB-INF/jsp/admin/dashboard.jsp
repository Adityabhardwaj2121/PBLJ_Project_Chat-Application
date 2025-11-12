<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Chat App</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <div class="admin-container">
        <div class="admin-header">
            <h1>Admin Dashboard</h1>
            <div class="admin-nav">
                <span>Welcome, ${currentUser.username}</span>
                <a href="${pageContext.request.contextPath}/chat" class="btn btn-secondary">Back to Chat</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary">Logout</a>
            </div>
        </div>
        
        <div class="admin-content">
            <!-- Statistics Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <h3>Total Users</h3>
                    <div class="stat-number">${totalUsers}</div>
                </div>
                <div class="stat-card">
                    <h3>Online Users</h3>
                    <div class="stat-number">${onlineUsers}</div>
                </div>
                <div class="stat-card">
                    <h3>Offline Users</h3>
                    <div class="stat-number">${totalUsers - onlineUsers}</div>
                </div>
            </div>
            
            <!-- Users Management -->
            <div class="admin-section">
                <h2>User Management</h2>
                <div class="table-container">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Status</th>
                                <th>Last Seen</th>
                                <th>Admin</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td>${user.userId}</td>
                                    <td>${user.username}</td>
                                    <td>${user.email}</td>
                                    <td>
                                        <span class="status-badge ${user.status.toLowerCase()}">${user.status}</span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.lastSeen != null}">
                                                ${user.lastSeen}
                                            </c:when>
                                            <c:otherwise>
                                                Never
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${user.admin}">
                                            <span class="admin-badge">Admin</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${user.userId != currentUser.userId}">
                                            <select onchange="updateUserStatus(${user.userId}, this.value)">
                                                <option value="Online" ${user.status == 'Online' ? 'selected' : ''}>Online</option>
                                                <option value="Offline" ${user.status == 'Offline' ? 'selected' : ''}>Offline</option>
                                                <option value="Busy" ${user.status == 'Busy' ? 'selected' : ''}>Busy</option>
                                            </select>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        function updateUserStatus(userId, newStatus) {
            fetch('${pageContext.request.contextPath}/admin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=updateUserStatus&userId=' + userId + '&status=' + newStatus
            })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    alert('User status updated successfully');
                    location.reload();
                } else {
                    alert('Failed to update user status: ' + (result.error || 'Unknown error'));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Failed to update user status');
            });
        }
    </script>
</body>
</html>