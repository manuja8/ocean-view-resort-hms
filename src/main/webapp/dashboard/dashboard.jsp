<%
    String role = (String) session.getAttribute("role");

    if(role == null){
        response.sendRedirect("login.jsp");
        return;
    }
%>


<h1>ADMIN DASHBOARD</h1>
<a href="../auth/login.jsp">Logout</a>
