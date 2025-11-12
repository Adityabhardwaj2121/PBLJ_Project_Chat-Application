<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat App - Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chat.css">
</head>
<body>
    <div class="chat-container">
        <!-- Sidebar -->
        <div class="sidebar">
            <div class="sidebar-header">
                <h3>Chat App</h3>
                <div class="user-info">
                    <span class="username">${currentUser.username}</span>
                    <span class="status online">Online</span>
                </div>
                <button class="logout-btn" onclick="logout()">Logout</button>
            </div>
            
            <!-- Search -->
            <div class="search-container">
                <input type="text" id="searchInput" placeholder="Search users or messages..." onkeyup="searchUsers()">
            </div>
            
            <!-- Tabs -->
            <div class="tabs">
                <button class="tab-btn active" onclick="showTab('users')">Users</button>
                <button class="tab-btn" onclick="showTab('groups')">Groups</button>
            </div>
            
            <!-- Users List -->
            <div id="users-tab" class="tab-content active">
                <div class="contact-list">
                    <c:forEach var="user" items="${users}">
                        <div class="contact-item" onclick="openChat(${user.userId}, '${user.username}', 'user')">
                            <div class="contact-avatar">${user.username.substring(0,1).toUpperCase()}</div>
                            <div class="contact-info">
                                <div class="contact-name">${user.username}</div>
                                <div class="contact-status ${user.status.toLowerCase()}">${user.status}</div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            
            <!-- Groups List -->
            <div id="groups-tab" class="tab-content">
                <div class="group-actions">
                    <button class="btn btn-primary create-group-btn" onclick="showCreateGroupModal()">Create Group</button>
                    <button class="btn btn-secondary search-group-btn" onclick="searchAndJoinGroups()">Search & Join Groups</button>
                </div>
                <div class="contact-list">
                    <c:forEach var="group" items="${groups}">
                        <div class="contact-item group-item" onclick="openChat(${group.groupId}, '${group.groupName}', 'group')">
                            <div class="contact-avatar">#</div>
                            <div class="contact-info">
                                <div class="contact-name">${group.groupName}</div>
                                <div class="contact-status">Group • ${group.description != null ? group.description : 'No description'}</div>
                            </div>
                            <div class="group-actions-menu">
                                <button class="btn-small btn-danger" onclick="event.stopPropagation(); leaveGroup(${group.groupId}, '${group.groupName}')" title="Leave Group">Leave</button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        
        <!-- Chat Area -->
        <div class="chat-area">
            <div class="welcome-message">
                <h2>Welcome to Chat App!</h2>
                <p>Select a user or group to start chatting.</p>
            </div>
            
            <!-- Chat Header -->
            <div class="chat-header" style="display: none;">
                <div class="chat-title">
                    <h3 id="chatTitle">Chat</h3>
                    <span id="chatStatus"></span>
                </div>
                <div class="chat-actions">
                    <button class="btn btn-secondary" onclick="searchMessages()">Search</button>
                    <button class="btn btn-secondary" onclick="viewHistory()">History</button>
                    <button id="groupInfoBtn" class="btn btn-secondary" onclick="showGroupInfo()" style="display: none;">Group Info</button>
                </div>
            </div>
            
            <!-- Messages Container -->
            <div class="messages-container" style="display: none;">
                <div id="messagesArea"></div>
            </div>
            
            <!-- Message Input -->
            <div class="message-input-container" style="display: none;">
                <div class="message-input">
                    <input type="text" id="messageInput" placeholder="Type a message..." onkeypress="handleKeyPress(event)">
                    <button class="btn btn-primary" onclick="sendMessage()">Send</button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Create Group Modal -->
    <div id="createGroupModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeCreateGroupModal()">&times;</span>
            <h2>Create New Group</h2>
            <form id="createGroupForm">
                <div class="form-group">
                    <label for="groupName">Group Name:</label>
                    <input type="text" id="groupName" required>
                </div>
                <div class="form-group">
                    <label for="groupDescription">Description:</label>
                    <textarea id="groupDescription" rows="3"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Create Group</button>
            </form>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/chat.js"></script>
    <script>
        // Set current user ID for JavaScript
        window.currentUserId = ${currentUser.userId};
        window.contextPath = '${pageContext.request.contextPath}';
    </script>
</body>
</html>