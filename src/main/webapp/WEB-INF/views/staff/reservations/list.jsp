<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Reservations</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />
</head>
<body>
<div class="app">
  <%@ include file="/WEB-INF/views/layouts/sidebar.jspf" %>
  <div class="main">
    <%@ include file="/WEB-INF/views/layouts/topbar.jspf" %>
    <div class="content">

      <div class="pagebar">
        <div>
          <h2>Reservations</h2>
          <div class="subtitle"></div>
        </div>

        <div class="toolbar">
          <form action="${pageContext.request.contextPath}/staff/reservations" method="get">
            <input class="input" type="text" name="q" placeholder="Search..." value="${param.q}" />

            <select class="select" name="status">
              <option value="">All Status</option>
              <option value="booked" ${param.status=='booked'?'selected':''}>Booked</option>
              <option value="checked_in" ${param.status=='checked_in'?'selected':''}>Checked In</option>
              <option value="checked_out" ${param.status=='checked_out'?'selected':''}>Checked Out</option>
              <option value="cancelled" ${param.status=='cancelled'?'selected':''}>Cancelled</option>
            </select>

            <button class="btn" type="submit">Search</button>
            <a class="btn" href="${pageContext.request.contextPath}/staff/reservations">Clear</a>
          </form>

          <a class="btn primary" href="${pageContext.request.contextPath}/staff/reservations?mode=create">Add Reservation</a>
        </div>
      </div>

      <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="alert success">${sessionScope.flashSuccess}</div>
        <c:remove var="flashSuccess" scope="session"/>
      </c:if>
      <c:if test="${not empty sessionScope.flashError}">
        <div class="alert error">${sessionScope.flashError}</div>
        <c:remove var="flashError" scope="session"/>
      </c:if>
      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="panel">
        <table class="table">
          <thead>
          <tr>
          <th>Reservation ID</th>
            <th>Res No</th>
            <th>Guest</th>
            <th>Room</th>
            <th>Check-In</th>
            <th>Check-Out</th>
            <th>Status</th>
            <th style="width:240px;">Actions</th>
          </tr>
          </thead>
          <tbody>

          <c:forEach var="r" items="${reservationList}">
            <tr>
            <td>${r.reservationNo}</td>
              <td style="font-weight:900; color:#0f172a;">${r.reservationNumber}</td>
              <td>${r.guest.fullName}</td>
              <td>${r.room.roomNumber}</td>
              <td>${r.checkIn}</td>
              <td>${r.checkOut}</td>
              <td>${r.status}</td>
              <td>
                <a class="btn small" href="${pageContext.request.contextPath}/staff/reservations?mode=edit&id=${r.reservationNo}">Edit</a>

                <form action="${pageContext.request.contextPath}/staff/reservations" method="post" style="display:inline;">
                  <input type="hidden" name="mode" value="cancel"/>
                  <input type="hidden" name="id" value="${r.reservationNo}"/>
                  <button class="btn small danger" type="submit"
                          onclick="return confirm('Cancel this reservation?');">
                    Cancel
                  </button>
                </form>

                <c:if test="${sessionScope.role == 'admin'}">
                  <form action="${pageContext.request.contextPath}/admin/reservations" method="post" style="display:inline;">
                    <input type="hidden" name="mode" value="delete"/>
                    <input type="hidden" name="id" value="${r.reservationNo}"/>
                    <button class="btn small danger" type="submit"
                            onclick="return confirm('Delete this reservation? (May fail if bills exist)');">
                      Delete
                    </button>
                  </form>
                </c:if>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty reservationList}">
            <tr><td colspan="7" style="color:#64748b; padding:16px;">No reservations found.</td></tr>
          </c:if>

          </tbody>
        </table>
      </div>

    </div>
    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>
</body>
</html>