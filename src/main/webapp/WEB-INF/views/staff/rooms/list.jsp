<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Rooms</title>
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
          <h2>Rooms</h2>
          <div class="subtitle">View and search rooms (Reception/Admin).</div>
        </div>

        <div class="toolbar">
          <form action="${pageContext.request.contextPath}/staff/rooms" method="get">
            <input class="input" type="text" name="q"
                   placeholder="Room no..." value="${param.q}" />

            <select class="select" name="status">
              <option value="">All Status</option>
              <option value="available" ${param.status=='available'?'selected':''}>Available</option>
              <option value="occupied" ${param.status=='occupied'?'selected':''}>Occupied</option>
              <option value="maintenance" ${param.status=='maintenance'?'selected':''}>Maintenance</option>
            </select>

            <button class="btn" type="submit">Search</button>
            <a class="btn" href="${pageContext.request.contextPath}/staff/rooms">Clear</a>
          </form>
        </div>
      </div>

      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="panel">
        <table class="table">
          <thead>
          <tr>
            <th>Room No</th>
            <th>Type</th>
            <th>Price</th>
            <th>Status</th>
          </tr>
          </thead>
          <tbody>

          <c:forEach var="r" items="${rooms}">
            <tr>
              <td style="font-weight:900; color:#0f172a;">${r.roomNumber}</td>
              <td>${r.roomTypeName}</td>
              <td>
                <c:choose>
                  <c:when test="${not empty r.price}">
                    LKR ${r.price}
                  </c:when>
                  <c:otherwise>—</c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${r.status == 'available'}">
                    <span class="pill ok"><span class="dot ok"></span>Available</span>
                  </c:when>
                  <c:when test="${r.status == 'occupied'}">
                    <span class="pill warn"><span class="dot warn"></span>Occupied</span>
                  </c:when>
                  <c:otherwise>
                    <span class="pill danger"><span class="dot danger"></span>Maintenance</span>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty rooms}">
            <tr>
              <td colspan="4" style="color:#64748b; padding:16px;">
                No rooms found for your search
              </td>
            </tr>
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