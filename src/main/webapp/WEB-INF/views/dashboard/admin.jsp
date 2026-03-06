<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Admin Dashboard" />

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Admin Dashboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />
</head>
<body>

<div class="app">
  <%@ include file="/WEB-INF/views/layouts/sidebar.jspf" %>

  <div class="main">
    <%@ include file="/WEB-INF/views/layouts/topbar.jspf" %>

    <div class="content">
      <h2 class="section-title">Management Overview</h2>
      <p class="section-hint">Administrator  hotel operations overview</p>

      <div class="kpi-row">

        <div class="kpi kpi--primary">
          <div class="kpi-top">
            <div class="label">Reservations (Today)</div>
            <div class="kpi-pill">Today</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Total reservations created for today</div>
        </div>

        <div class="kpi kpi--good">
          <div class="kpi-top">
            <div class="label">Available Rooms</div>
            <div class="kpi-pill">Live</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Rooms currently available for booking</div>
        </div>

        <div class="kpi kpi--primary">
          <div class="kpi-top">
            <div class="label">Occupied Rooms</div>
            <div class="kpi-pill">Live</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Rooms currently occupied (checked-in)</div>
        </div>

        <div class="kpi kpi--danger">
          <div class="kpi-top">
            <div class="label">Maintenance Rooms</div>
            <div class="kpi-pill">Live</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Rooms under maintenance</div>
        </div>

        <div class="kpi kpi--warn">
          <div class="kpi-top">
            <div class="label">Pending Payments</div>
            <div class="kpi-pill">Live</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Payments waiting for completion</div>
        </div>

        <div class="kpi kpi--good">
          <div class="kpi-top">
            <div class="label">Registered Guests</div>
            <div class="kpi-pill">Total</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Total guests in the system</div>
        </div>

      </div>
    <div class="activity-row">

      <div class="panel">
        <h3>Recent Activity</h3>
        <p class="section-hint" style="margin-top:4px;">Latest system events</p>

        <table class="table">
          <thead>
            <tr>
              <th>Time</th>
              <th>Activity</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>09:10</td>
              <td>User account updated (Receptionist permissions)</td>
              <td><span class="pill ok"><span class="p"></span> Completed</span></td>
            </tr>
            <tr>
              <td>10:05</td>
              <td>Room marked as Maintenance (AC Repair)</td>
              <td><span class="pill warn"><span class="p"></span> Attention</span></td>
            </tr>
            <tr>
              <td>11:30</td>
              <td>Daily report generated</td>
              <td><span class="pill ok"><span class="p"></span> Completed</span></td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="panel">
        <h3>Announcements</h3>
        <div class="announce">
          <div class="announce-item">
            <b>Maintenance Reminder</b>
            <span>Verify room status updates before peak hours</span>
          </div>
          <div class="announce-item">
            <b>Security</b>
            <span>Ensure your login credentials remain confidential do not share with others</span>
          </div>
          <div class="announce-item">
            <b>Daily Task</b>
            <span>Generate revenue and payment summary report</span>
          </div>
        </div>
      </div>

    </div>
    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>

</body>
</html>