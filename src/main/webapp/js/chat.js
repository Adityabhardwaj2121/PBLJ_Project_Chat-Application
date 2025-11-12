// Global variables
let currentChatId = null;
let currentChatType = null; // 'user' or 'group'
let currentChatName = null;
let messagePollingInterval = null;

// Initialize chat functionality
document.addEventListener('DOMContentLoaded', function() {
    // Start polling for new messages every 3 seconds
    setInterval(pollForMessages, 3000);
});

// Tab switching
function showTab(tabName) {
    // Hide all tab contents
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    
    // Remove active class from all tab buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Show selected tab
    document.getElementById(tabName + '-tab').classList.add('active');
    event.target.classList.add('active');
}

// Open chat with user or group
function openChat(id, name, type) {
    currentChatId = id;
    currentChatType = type;
    currentChatName = name;
    
    // Update UI
    document.querySelector('.welcome-message').style.display = 'none';
    document.querySelector('.chat-header').style.display = 'flex';
    document.querySelector('.messages-container').style.display = 'block';
    document.querySelector('.message-input-container').style.display = 'block';
    
    // Update chat title and status
    document.getElementById('chatTitle').textContent = name;
    if (type === 'group') {
        document.getElementById('chatStatus').textContent = 'Group Chat';
        document.getElementById('groupInfoBtn').style.display = 'inline-block';
    } else {
        document.getElementById('chatStatus').textContent = 'Private Chat';
        document.getElementById('groupInfoBtn').style.display = 'none';
    }
    
    // Highlight selected contact
    document.querySelectorAll('.contact-item').forEach(item => {
        item.classList.remove('active');
    });
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }
    
    // Load messages
    loadMessages();
}

// Load messages for current chat
function loadMessages() {
    if (!currentChatId || !currentChatType) return;
    
    const params = new URLSearchParams();
    params.append('action', 'getMessages');
    
    if (currentChatType === 'user') {
        params.append('receiverId', currentChatId);
    } else {
        params.append('groupId', currentChatId);
    }
    
    fetch(window.contextPath + '/message', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(messages => {
        displayMessages(messages);
    })
    .catch(error => {
        console.error('Error loading messages:', error);
    });
}

// Display messages in chat area
function displayMessages(messages) {
    const messagesArea = document.getElementById('messagesArea');
    messagesArea.innerHTML = '';
    
    messages.forEach(message => {
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message' + (message.senderId === window.currentUserId ? ' own' : '');
        
        // For group messages, show sender name if it's not the current user
        if (currentChatType === 'group' && message.senderId !== window.currentUserId) {
            const senderName = document.createElement('div');
            senderName.className = 'message-sender';
            senderName.textContent = message.senderUsername || ('User ' + message.senderId);
            messageDiv.appendChild(senderName);
        }
        
        const messageContent = document.createElement('div');
        messageContent.className = 'message-content';
        messageContent.textContent = message.content;
        
        const messageInfo = document.createElement('div');
        messageInfo.className = 'message-info';
        messageInfo.textContent = formatTimestamp(message.timestamp);
        
        messageDiv.appendChild(messageContent);
        messageDiv.appendChild(messageInfo);
        messagesArea.appendChild(messageDiv);
    });
    
    // Scroll to bottom
    messagesArea.scrollTop = messagesArea.scrollHeight;
}

// Send message
function sendMessage() {
    const messageInput = document.getElementById('messageInput');
    const content = messageInput.value.trim();
    
    if (!content || !currentChatId || !currentChatType) return;
    
    const params = new URLSearchParams();
    params.append('action', 'send');
    params.append('content', content);
    params.append('messageType', 'text');
    
    if (currentChatType === 'user') {
        params.append('receiverId', currentChatId);
    } else {
        params.append('groupId', currentChatId);
    }
    
    fetch(window.contextPath + '/message', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            messageInput.value = '';
            loadMessages(); // Reload messages to show the new one
        } else {
            alert('Failed to send message: ' + (result.error || 'Unknown error'));
        }
    })
    .catch(error => {
        console.error('Error sending message:', error);
        alert('Failed to send message');
    });
}

// Handle Enter key press in message input
function handleKeyPress(event) {
    if (event.key === 'Enter') {
        sendMessage();
    }
}

// Poll for new messages
function pollForMessages() {
    if (currentChatId && currentChatType) {
        loadMessages();
    }
}

// Search users
function searchUsers() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const contactItems = document.querySelectorAll('.contact-item');
    
    contactItems.forEach(item => {
        const contactName = item.querySelector('.contact-name').textContent.toLowerCase();
        if (contactName.includes(searchTerm)) {
            item.style.display = 'flex';
        } else {
            item.style.display = 'none';
        }
    });
}

// Search messages
function searchMessages() {
    const searchTerm = prompt('Enter search term:');
    if (!searchTerm) return;
    
    const params = new URLSearchParams();
    params.append('action', 'search');
    params.append('searchTerm', searchTerm);
    
    fetch(window.contextPath + '/message', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(messages => {
        if (messages.length > 0) {
            displayMessages(messages);
        } else {
            alert('No messages found containing: ' + searchTerm);
        }
    })
    .catch(error => {
        console.error('Error searching messages:', error);
    });
}

// View chat history
function viewHistory() {
    if (currentChatId && currentChatType === 'user') {
        // For private chats, we can show XML history
        alert('Chat history feature - would load XML logs for user ' + currentChatId);
    } else {
        loadMessages(); // Just reload current messages for now
    }
}

// Create group modal functions
function showCreateGroupModal() {
    document.getElementById('createGroupModal').style.display = 'block';
}

function closeCreateGroupModal() {
    document.getElementById('createGroupModal').style.display = 'none';
    document.getElementById('createGroupForm').reset();
}

// Handle create group form submission
document.getElementById('createGroupForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const groupName = document.getElementById('groupName').value.trim();
    const groupDescription = document.getElementById('groupDescription').value.trim();
    
    if (!groupName) {
        alert('Group name is required');
        return;
    }
    
    // Send request to create the group
    const params = new URLSearchParams();
    params.append('action', 'create');
    params.append('groupName', groupName);
    params.append('description', groupDescription);
    
    fetch(window.contextPath + '/group', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            alert('Group "' + result.groupName + '" created successfully!');
            closeCreateGroupModal();
            // Reload the page to show the new group
            location.reload();
        } else {
            alert('Failed to create group: ' + (result.error || 'Unknown error'));
        }
    })
    .catch(error => {
        console.error('Error creating group:', error);
        alert('Failed to create group');
    });
});

// Join group function
function joinGroup(groupId, groupName) {
    if (confirm('Do you want to join the group "' + groupName + '"?')) {
        const params = new URLSearchParams();
        params.append('action', 'join');
        params.append('groupId', groupId);
        
        fetch(window.contextPath + '/group', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('Successfully joined group "' + groupName + '"!');
                location.reload();
            } else {
                alert('Failed to join group: ' + (result.error || 'Unknown error'));
            }
        })
        .catch(error => {
            console.error('Error joining group:', error);
            alert('Failed to join group');
        });
    }
}

// Leave group function
function leaveGroup(groupId, groupName) {
    if (confirm('Are you sure you want to leave the group "' + groupName + '"?')) {
        const params = new URLSearchParams();
        params.append('action', 'leave');
        params.append('groupId', groupId);
        
        fetch(window.contextPath + '/group', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: params
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('Successfully left group "' + groupName + '"!');
                location.reload();
            } else {
                alert('Failed to leave group: ' + (result.error || 'Unknown error'));
            }
        })
        .catch(error => {
            console.error('Error leaving group:', error);
            alert('Failed to leave group');
        });
    }
}

// Search and join groups function
function searchAndJoinGroups() {
    const searchTerm = prompt('Enter group name to search:');
    if (!searchTerm) return;
    
    const params = new URLSearchParams();
    params.append('action', 'search');
    params.append('searchTerm', searchTerm);
    
    fetch(window.contextPath + '/group', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(groups => {
        if (groups.length > 0) {
            let groupList = 'Available groups:\n\n';
            groups.forEach((group, index) => {
                groupList += `${index + 1}. ${group.groupName}\n   Description: ${group.description || 'No description'}\n\n`;
            });
            
            const choice = prompt(groupList + 'Enter the number of the group you want to join (or cancel):');
            if (choice && !isNaN(choice)) {
                const selectedIndex = parseInt(choice) - 1;
                if (selectedIndex >= 0 && selectedIndex < groups.length) {
                    const selectedGroup = groups[selectedIndex];
                    joinGroup(selectedGroup.groupId, selectedGroup.groupName);
                }
            }
        } else {
            alert('No groups found containing: ' + searchTerm);
        }
    })
    .catch(error => {
        console.error('Error searching groups:', error);
        alert('Failed to search groups');
    });
}

// Show group information
function showGroupInfo() {
    if (currentChatType !== 'group' || !currentChatId) return;
    
    const params = new URLSearchParams();
    params.append('action', 'getMembers');
    params.append('groupId', currentChatId);
    
    fetch(window.contextPath + '/group', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(memberIds => {
        let info = `Group: ${currentChatName}\n\n`;
        info += `Members: ${memberIds.length}\n`;
        info += `Member IDs: ${memberIds.join(', ')}\n\n`;
        info += 'Group Actions:\n';
        info += '• Send messages to all members\n';
        info += '• View group chat history\n';
        info += '• Leave group (from sidebar)';
        
        alert(info);
    })
    .catch(error => {
        console.error('Error getting group info:', error);
        alert('Failed to get group information');
    });
}

// Logout function
function logout() {
    if (confirm('Are you sure you want to logout?')) {
        window.location.href = window.contextPath + '/logout';
    }
}

// Utility function to format timestamp
function formatTimestamp(timestamp) {
    if (!timestamp) return '';
    
    const date = new Date(timestamp);
    const now = new Date();
    const diffInHours = (now - date) / (1000 * 60 * 60);
    
    if (diffInHours < 24) {
        return date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
    } else {
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
    }
}

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('createGroupModal');
    if (event.target === modal) {
        closeCreateGroupModal();
    }
}