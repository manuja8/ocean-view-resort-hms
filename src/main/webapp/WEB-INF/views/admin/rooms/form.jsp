<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Room Form" />

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Room Form</title>
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
          <h2>
            <c:choose>
              <c:when test="${param.mode == 'edit'}">Update Room</c:when>
              <c:otherwise>Add Room</c:otherwise>
            </c:choose>
          </h2>
          <div class="subtitle">Admin-only room setup and status.</div>
        </div>

        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/admin/rooms">Back</a>
        </div>
      </div>

      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="panel form-panel">
        <form action="${pageContext.request.contextPath}/admin/rooms" method="post">
         <input type="hidden" name="mode" value="${empty param.mode ? requestScope.mode : param.mode}" />
         <input type="hidden" name="id" value="${empty param.id ? requestScope.room.roomId : param.id}" />

          <div class="form-grid">

            <div class="field">
              <label>Room Number</label>
              <input class="input" type="text" name="roomNumber" value="${requestScope.room.roomNumber}" placeholder="e.g. 101" required />
            </div>

            <div class="field">
              <label>Room Type</label>
              <select class="select" name="roomTypeId" required>
                <option value="">-- Select Type --</option>
                <c:forEach var="t" items="${requestScope.roomTypes}">
                  <option value="${t.roomTypeId}" ${t.roomTypeId == requestScope.room.roomTypeId ? 'selected' : ''}>
                    ${t.typeName} (${t.price})
                  </option>
                </c:forEach>
              </select>
              <c:if test="${empty requestScope.roomTypes}">
                <div class="help">Room types will load from DB (room_types table).</div>
              </c:if>
            </div>

            <div class="field full">
              <label>Status</label>
              <select class="select" name="status" required>
                <option value="available" ${requestScope.room.status=='available'?'selected':''}>Available</option>
                <option value="occupied" ${requestScope.room.status=='occupied'?'selected':''}>Occupied</option>
                <option value="maintenance" ${requestScope.room.status=='maintenance'?'selected':''}>Maintenance</option>
              </select>
            </div>

          </div>

          <div class="toolbar" style="margin-top:14px;">
            <button class="btn primary" type="submit">Save</button>
            <a class="btn" href="${pageContext.request.contextPath}/admin/rooms">Cancel</a>
          </div>
        </form>
      </div>

    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>

</body>
</html>