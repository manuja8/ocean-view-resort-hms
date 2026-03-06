<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Receptionist Dashboard" />

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Receptionist Dashboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />
</head>
<body>

<div class="app">
  <%@ include file="/WEB-INF/views/layouts/sidebar.jspf" %>

  <div class="main">
    <%@ include file="/WEB-INF/views/layouts/topbar.jspf" %>

    <div class="content">
      <h2 class="section-title">Reception Overview</h2>
      <p class="section-hint">Front desk daily operations overview.</p>

      <div class="kpi-row">

        <div class="kpi kpi--primary">
          <div class="kpi-top">
            <div class="label">Check-ins (Today)</div>
            <div class="kpi-pill">Today</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Reservations checked in today.</div>
        </div>

        <div class="kpi kpi--primary">
          <div class="kpi-top">
            <div class="label">Check-outs (Today)</div>
            <div class="kpi-pill">Today</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Reservations checked out today.</div>
        </div>

        <div class="kpi kpi--good">
          <div class="kpi-top">
            <div class="label">Available Rooms</div>
            <div class="kpi-pill">Live</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Rooms available for walk-ins.</div>
        </div>

        <div class="kpi kpi--warn">
          <div class="kpi-top">
            <div class="label">Bills to Print</div>
            <div class="kpi-pill">Live</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Bills awaiting printing.</div>
        </div>

        <div class="kpi kpi--primary">
          <div class="kpi-top">
            <div class="label">Active Reservations</div>
            <div class="kpi-pill">Live</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Booked + checked-in reservations.</div>
        </div>

        <div class="kpi kpi--warn">
          <div class="kpi-top">
            <div class="label">Pending Payments</div>
            <div class="kpi-pill">Live</div>
          </div>
          <div class="value">—</div>
          <div class="sub">Payments waiting for completion.</div>
        </div>

      </div>

      <div class="activity-row">

        <div class="panel">
          <h3>Front Desk Activity</h3>
          <p class="section-hint" style="margin-top:4px;">Recent actions & priorities.</p>

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
                <td>09:20</td>
                <td>Guest added (ID verified)</td>
                <td><span class="pill ok"><span class="p"></span> Completed</span></td>
              </tr>
              <tr>
                <td>10:00</td>
                <td>Reservation created (Room allocation pending)</td>
                <td><span class="pill warn"><span class="p"></span> Pending</span></td>
              </tr>
              <tr>
                <td>11:05</td>
                <td>Bill calculated for checkout</td>
                <td><span class="pill ok"><span class="p"></span> Completed</span></td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="panel">
          <h3>Notices</h3>
          <div class="announce">
            <div class="announce-item">
              <b>Check-in Tip</b>
              <span>Confirm guest ID and reservation dates before assigning room.</span>
            </div>
            <div class="announce-item">
              <b>Billing Tip</b>
              <span>Always calculate bill before starting payment processing.</span>
            </div>
            <div class="announce-item">
              <b>Room Status</b>
              <span>Rooms in maintenance should not be booked.</span>
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