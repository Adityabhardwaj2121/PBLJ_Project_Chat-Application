# 💬 Real-Time Chat Application

A comprehensive, production-ready chat application built with **Java Servlets**, **JSP**, **MySQL**, and **XML logging**. Features both one-to-one and group messaging with a modern, responsive web interface.

![Java](https://img.shields.io/badge/Java-8+-orange) ![MySQL](https://img.shields.io/badge/MySQL-8.0-blue) ![Tomcat](https://img.shields.io/badge/Tomcat-8.5-green) 

## ✨ Features

### 🔐 **Authentication & Security**
- ✅ User Registration & Login
- ✅ Session Management
- ✅ Authentication Filters
- ✅ SQL Injection Protection
- ✅ Admin Panel Access Control

### 💬 **Messaging**
- ✅ **Real-time Private Chat** - One-to-one messaging
- ✅ **Group Chat** - Create, join, and manage groups
- ✅ **Message History** - Database + XML backup storage
- ✅ **Message Search** - Find messages by keyword
- ✅ **Typing Indicators** - Real-time status updates

### 👥 **User Management**
- ✅ **Online Status** - See who's online/offline/busy
- ✅ **User Search** - Find and connect with users
- ✅ **Profile Management** - User information display
- ✅ **Last Seen** - Track user activity

### 🎛️ **Admin Features**
- ✅ **Admin Dashboard** - User management interface
- ✅ **User Statistics** - Online/offline user counts
- ✅ **Status Management** - Update user statuses
- ✅ **System Monitoring** - Track application usage

### 🎨 **User Interface**
- ✅ **Responsive Design** - Mobile-first approach
- ✅ **Modern UI** - Clean, intuitive interface
- ✅ **Real-time Updates** - Auto-refresh every 3 seconds
- ✅ **Dark/Light Theme** - Professional styling

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|------------|---------|
| **Backend** | Java Servlets | 4.0 |
| **Frontend** | JSP, HTML5, CSS3, JavaScript | Latest |
| **Database** | MySQL | 8.0+ |
| **Server** | Apache Tomcat | 8.5+ |
| **Build Tool** | Maven | 3.6+ |
| **Message Storage** | XML + Database | Custom |

## 🚀 Quick Start Guide

### Prerequisites
- ✅ **XAMPP** (includes Apache, MySQL, Tomcat)
- ✅ **Java JDK 8+**
- ✅ **Maven 3.6+** (optional, for building)

### 📥 Installation Steps

#### 1️⃣ **Download & Install XAMPP**
```bash
# Download from: https://www.apachefriends.org/
# Install and start Apache + MySQL services
```

#### 2️⃣ **Setup Database**
```sql
-- Open phpMyAdmin (http://localhost/phpmyadmin)
-- Create new database: chatapp_db
-- Import: database/schema.sql
```

#### 3️⃣ **Build Application**
```bash
# Clone/download the project
cd ChatApp

# Build with Maven (if available)
mvn clean package

# OR use the pre-built ChatApp.war file
```

#### 4️⃣ **Deploy to Tomcat**
```bash
# Copy WAR file to XAMPP Tomcat
copy ChatApp.war "C:\xampp\tomcat\webapps\"

# OR for custom XAMPP location
copy ChatApp.war "E:\xamppp\tomcat\webapps\"
```

#### 5️⃣ **Start Services**
```bash
# Start XAMPP Tomcat with correct environment
$env:CATALINA_HOME="E:\xamppp\tomcat"
$env:CATALINA_BASE="E:\xamppp\tomcat"
& "E:\xamppp\tomcat\bin\startup.bat"
```

#### 6️⃣ **Access Application**
```
🌐 Open browser: http://localhost:8080/ChatApp
```

## 🎯 Default Test Accounts

### 👨‍💼 **Admin Account**
```
Username: admin
Password: admin123
Role: Administrator
```


## 📱 How to Use

### **Getting Started**
1. **Login** with any test account above
2. **Explore** the clean, intuitive interface
3. **Start chatting** immediately with pre-loaded users

### **Private Messaging**
1. Click **"Users"** tab in sidebar
2. Select any **online user**
3. **Type and send** messages instantly
4. **Search messages** using the search button

### **Group Chat**
1. Click **"Groups"** tab in sidebar
2. **Create new group** or **join existing ones**
3. **Search & Join Groups** to find public groups
4. **Group messaging** with sender names displayed
5. **Leave groups** easily with hover actions

### **Admin Features** (admin account only)
1. Access **Admin Dashboard** from chat interface
2. **View user statistics** and online status
3. **Manage user accounts** and permissions
4. **Monitor system activity**

## 🏗️ Project Structure

```
ChatApp/
├── 📁 src/main/java/com/chatapp/
│   ├── 📁 dao/                    # Data Access Objects
│   │   ├── UserDAO.java           # User database operations
│   │   ├── MessageDAO.java        # Message CRUD operations  
│   │   └── GroupDAO.java          # Group management
│   ├── 📁 filter/                 # Security & Authentication
│   │   └── AuthenticationFilter.java
│   ├── 📁 model/                  # Data Models
│   │   ├── User.java              # User entity
│   │   ├── Message.java           # Message entity
│   │   └── Group.java             # Group entity
│   ├── 📁 servlet/                # HTTP Controllers
│   │   ├── LoginServlet.java      # Authentication
│   │   ├── ChatServlet.java       # Main chat interface
│   │   ├── MessageServlet.java    # Message API
│   │   ├── GroupServlet.java      # Group management API
│   │   └── AdminServlet.java      # Admin panel
│   └── 📁 util/                   # Utilities
│       ├── DatabaseConnection.java # MySQL connection
│       └── XMLMessageLogger.java   # XML backup system
├── 📁 src/main/webapp/
│   ├── 📁 css/                    # Stylesheets
│   │   ├── style.css              # Global styles
│   │   ├── chat.css               # Chat interface
│   │   └── admin.css              # Admin panel
│   ├── 📁 js/                     # JavaScript
│   │   └── chat.js                # Chat functionality
│   └── 📁 WEB-INF/jsp/            # JSP Templates
│       ├── login.jsp              # Login page
│       ├── register.jsp           # Registration
│       ├── chat.jsp               # Main chat interface
│       └── admin/dashboard.jsp    # Admin panel
├── 📁 database/
│   └── schema.sql                 # Complete database schema
├── 📁 xml-logs/                   # Auto-generated XML backups
├── 📄 pom.xml                     # Maven configuration
└── 📄 ChatApp.war                 # Deployable application
```

## 🔧 Configuration

### **Database Settings**
```java
// File: src/main/java/com/chatapp/util/DatabaseConnection.java
private static final String URL = "jdbc:mysql://localhost:3306/chatapp_db";
private static final String USERNAME = "root";
private static final String PASSWORD = ""; // Default XAMPP password
```

### **Tomcat Configuration**
```xml
<!-- File: src/main/webapp/WEB-INF/web.xml -->
<!-- Session timeout: 30 minutes -->
<!-- Security constraints for protected routes -->
```

## 🧪 Testing Features

### **Test Private Messaging**
1. Login as `john_doe`
2. Send message to `jane_smith`
3. Login as `jane_smith` in another browser
4. Verify real-time message delivery

### **Test Group Functionality**
1. Login as `admin` → Create group "Test Group"
2. Login as `john_doe` → Search and join "Test Group"
3. Login as `jane_smith` → Join same group
4. Send messages from different users
5. Verify sender names and timestamps

### **Test Admin Features**
1. Login as `admin`
2. Access Admin Dashboard
3. View user statistics
4. Update user statuses
5. Monitor system activity

## 🚨 Troubleshooting

### **Common Issues & Solutions**

#### **❌ 404 Error - Application Not Found**
```bash
# Solution: Ensure Tomcat is running and WAR is deployed
# Check: http://localhost:8080/ should show Tomcat page
# Verify: ChatApp.war exists in webapps directory
```

#### **❌ 500 Error - Internal Server Error**
```bash
# Solution: Check database connection
# Verify: MySQL is running in XAMPP
# Check: Database 'chatapp_db' exists with proper schema
```

#### **❌ Database Connection Failed**
```bash
# Solution: Update database credentials
# File: src/main/java/com/chatapp/util/DatabaseConnection.java
# Verify: MySQL service is running on port 3306
```

#### **❌ Login Issues**
```bash
# Solution: Use correct test accounts
# Verify: Database has user records
# Check: Password matches (no encryption implemented)
```

#### **❌ Messages Not Loading**
```bash
# Solution: Check browser console for errors
# Verify: JavaScript is enabled
# Check: Network requests are successful
```

## 🔒 Security Notes

### **⚠️ Development vs Production**
- **Passwords**: Currently stored in plain text (implement hashing for production)
- **HTTPS**: Enable SSL/TLS for production deployment  
- **Session Security**: Consider JWT tokens for enhanced security
- **Input Validation**: Additional sanitization recommended
- **Rate Limiting**: Implement for production use

### **✅ Implemented Security**
- SQL injection protection with PreparedStatements
- Authentication filters for protected routes
- Session management with timeout
- XSS prevention in JSP templates

## 📊 Database Schema

### **Core Tables**
- **users** - User accounts and authentication
- **groups** - Chat group definitions  
- **group_members** - Group membership relationships
- **messages** - All chat messages (private + group)
- **user_sessions** - Session tracking
- **notifications** - User notification system

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request


## 🎉 Ready to Chat!

Your real-time chat application is now ready for use! 

**🌐 Access URL**: `http://localhost:8080/ChatApp`

**📧 Support**: For issues or questions, check the troubleshooting section or create an issue.

---

**Built with ❤️ using Java, Servlets, JSP, and MySQL**
