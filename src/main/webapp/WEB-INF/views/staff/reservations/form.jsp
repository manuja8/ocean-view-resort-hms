<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Reservation Form</title>
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
          <h2><c:choose><c:when test="${mode=='edit'}">Update Reservation</c:when><c:otherwise>Add Reservation</c:otherwise></c:choose></h2>
          <div class="subtitle">Reservation details.</div>
        </div>
        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/staff/reservations">Back</a>
        </div>
      </div>

      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="panel form-panel">
        <form action="${pageContext.request.contextPath}/staff/reservations" method="post">
          <input type="hidden" name="mode" value="${mode}" />
          <c:if test="${mode=='edit'}">
            <input type="hidden" name="id" value="${reservation.reservationNo}" />
          </c:if>

          <div class="form-grid">

            <div class="field">
              <label>Guest</label>
              <select class="select" name="guestId" required>
                <option value="">-- Select Guest --</option>
                <c:forEach var="g" items="${guests}">
                  <option value="${g.guestId}" ${reservation.guest.guestId==g.guestId?'selected':''}>
                    ${g.fullName} (${g.contactNo})
                  </option>
                </c:forEach>
              </select>
            </div>

            <div class="field">
              <label>Room</label>
              <select class="select" name="roomId" required>
                <option value="">-- Select Room --</option>
                <c:forEach var="rm" items="${rooms}">
                  <option value="${rm.roomId}" ${reservation.room.roomId==rm.roomId?'selected':''}>
                    ${rm.roomNumber} - ${rm.roomTypeName} (LKR ${rm.price}) [${rm.status}]
                  </option>
                </c:forEach>
              </select>
            </div>

            <div class="field">
              <label>Check-In</label>
              <input class="input" type="date" name="checkIn" value="${reservation.checkIn}" required/>
            </div>

            <div class="field">
              <label>Check-Out</label>
              <input class="input" type="date" name="checkOut" value="${reservation.checkOut}" required/>
            </div>

            <div class="field full">
              <label>Status</label>
              <select class="select" name="status" required>
                <option value="booked" ${reservation.status=='booked'?'selected':''}>Booked</option>
                <option value="checked_in" ${reservation.status=='checked_in'?'selected':''}>Checked In</option>
                <option value="checked_out" ${reservation.status=='checked_out'?'selected':''}>Checked Out</option>
                <option value="cancelled" ${reservation.status=='cancelled'?'selected':''}>Cancelled</option>
              </select>
              <div class="subtitle" style="margin-top:6px;">Create defaults to “Booked”.</div>
            </div>

          </div>

          <div class="toolbar" style="margin-top:14px;">
            <button class="btn primary" type="submit">Save</button>
            <a class="btn" href="${pageContext.request.contextPath}/staff/reservations">Cancel</a>
          </div>

        </form>
      </div>

    </div>
    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>
</body>
</html>