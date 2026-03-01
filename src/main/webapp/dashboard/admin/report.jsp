<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.oceanview.dto.ReportDTO" %>
<%
    String role = (String) session.getAttribute("role");
    if (role == null || !role.equalsIgnoreCase("admin")) {
        response.sendRedirect(request.getContextPath() + "/dashboard/dashboard.jsp");
        return;
    }

    ReportDTO report = (ReportDTO) request.getAttribute("report");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Report | Ocean View HMS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="<%=request.getContextPath()%>/dashboard/dashboard.jsp">Ocean View HMS</a>
        <div class="d-flex">
            <span class="navbar-text text-white me-3">Admin</span>
            <a class="btn btn-outline-light btn-sm" href="<%=request.getContextPath()%>/logout">Logout</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <h2>Reports Overview</h2>
    <div class="row mt-4">
        <div class="col-md-4">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Total Reservations</h5>
                    <p class="card-text display-6"><%= report != null ? report.getTotalReservations() : 0 %></p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Total Guests</h5>
                    <p class="card-text display-6"><%= report != null ? report.getTotalGuests() : 0 %></p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Total Rooms</h5>
                    <p class="card-text display-6"><%= report != null ? report.getTotalRooms() : 0 %></p>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-md-4">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Total Bills</h5>
                    <p class="card-text display-6"><%= report != null ? report.getTotalBills() : 0 %></p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Paid Bills</h5>
                    <p class="card-text display-6"><%= report != null ? report.getPaidBills() : 0 %></p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Unpaid Bills</h5>
                    <p class="card-text display-6"><%= report != null ? report.getUnpaidBills() : 0 %></p>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-md-6">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Total Payments</h5>
                    <p class="card-text display-6"><%= report != null ? report.getTotalPayments() : 0 %></p>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card text-center shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Total Revenue</h5>
                    <p class="card-text display-6">$<%= report != null ? report.getTotalRevenue() : 0.0 %></p>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>