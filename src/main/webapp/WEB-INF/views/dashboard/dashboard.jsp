<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>OceanView HMS - Dashboard</title>
</head>
<body>

<c:choose>
    <c:when test="${sessionScope.role == 'admin'}">
        <h1>ADMIN DASHBOARD</h1>
    </c:when>
    <c:when test="${sessionScope.role == 'receptionist'}">
        <h1>RECEPTIONIST DASHBOARD</h1>
    </c:when>
    <c:otherwise>
        <h1>DASHBOARD</h1>
    </c:otherwise>
</c:choose>

<p>Welcome, <b>${sessionScope.username}</b></p>

<a href="${pageContext.request.contextPath}/logout">Logout</a>

</body>
</html>