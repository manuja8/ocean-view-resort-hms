<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.dto.UserDTO" %>

<%
    List<UserDTO> users = (List<UserDTO>) request.getAttribute("users");
    String role = (String) session.getAttribute("role");
%>

<h1>User Management</h1>

<form method="get" action="">
    <input type="text" name="search" placeholder="Search username">
    <button type="submit">Search</button>
</form>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Role</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.userId}</td>
            <td>${user.username}</td>
            <td>${user.role}</td>
            <td>
                <c:if test="${role eq 'admin'}">
                    <form method="post" style="display:inline;">
                        <input type="hidden" name="userId" value="${user.userId}">
                        <button name="action" value="delete">Delete</button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

<c:if test="${role eq 'admin'}">
    <h2>Add User</h2>
    <form method="post">
        <input type="text" name="username" placeholder="Username" required>
        <input type="password" name="password" placeholder="Password" required>
        <select name="role">
            <option value="admin">Admin</option>
            <option value="receptionist">Receptionist</option>
        </select>
        <button name="action" value="create">Create</button>
    </form>
</c:if>