<%@ page session="true" %>
<%
    String role = (String) session.getAttribute("role");

    if (role == null) {
        response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
        return;
    }
%>

<h1>Welcome, <%= session.getAttribute("username") %></h1>
<h2>Role: <%= role %></h2>

<hr>

<h3>Core Modules</h3>

<a href="<%=request.getContextPath()%>/dashboard/reservation">Reservations</a><br>
<a href="<%=request.getContextPath()%>/dashboard/guest">Guests</a><br>
<a href="<%=request.getContextPath()%>/dashboard/room">Rooms</a><br>
<a href="<%=request.getContextPath()%>/dashboard/billing">Billing</a><br>
<a href="<%=request.getContextPath()%>/dashboard/payment">Payment</a><br>
<a href="<%=request.getContextPath()%>/dashboard/complaint">Complaint</a><br>

<%
    if ("ADMIN".equalsIgnoreCase(role)) {
%>

<hr>
<h3>Admin Modules</h3>

<a href="<%=request.getContextPath()%>/dashboard/admin/users">User Management</a><br>
<a href="<%=request.getContextPath()%>/dashboard/admin/report">Reports</a>

<%
    }
%>

<hr>
<a href="<%=request.getContextPath()%>/logout">Logout</a>