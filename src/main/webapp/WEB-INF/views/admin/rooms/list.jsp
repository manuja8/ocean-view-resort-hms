<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Rooms Management" />

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
          <h2>Rooms Management</h2>
          <div class="subtitle">Admin-only: add, update, delete rooms.</div>
        </div>

       <div class="toolbar">
         <form action="${pageContext.request.contextPath}/admin/rooms" method="get">
           <input class="input" type="text" name="q"
                  placeholder="Room no..." value="${param.q}" />

           <select class="select" name="status">
             <option value="">All Status</option>
             <option value="available" ${param.status=='available'?'selected':''}>Available</option>
             <option value="occupied" ${param.status=='occupied'?'selected':''}>Occupied</option>
             <option value="maintenance" ${param.status=='maintenance'?'selected':''}>Maintenance</option>
           </select>


           <button class="btn" type="submit">Search</button>

           <!-- Optional: clear filters (nice UX) -->
           <a class="btn" href="${pageContext.request.contextPath}/admin/rooms">Clear</a>
         </form>

         <a class="btn primary" href="${pageContext.request.contextPath}/admin/rooms?mode=create">Add Room</a>
       </div>
      </div>

      <c:if test="${not empty success}">
        <div class="alert success">${success}</div>
      </c:if>
      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="panel">
        <table class="table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Room No</th>
            <th>Type</th>
            <th>Price</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
          </thead>

          <tbody>
          <c:forEach var="r" items="${requestScope.rooms}">
            <tr>
              <td>${r.roomId}</td>
              <td>${r.roomNumber}</td>
              <td>${r.roomTypeName}</td>
              <td>${r.price}</td>
              <td>
                <c:choose>
                  <c:when test="${r.status == 'available'}">
                    <span class="pill ok"><span class="dot"></span>Available</span>
                  </c:when>
                  <c:when test="${r.status == 'occupied'}">
                    <span class="pill warn"><span class="dot"></span>Occupied</span>
                  </c:when>
                  <c:otherwise>
                    <span class="pill danger"><span class="dot"></span>Maintenance</span>
                  </c:otherwise>
                </c:choose>
              </td>

              <td>
                <div class="toolbar">
                  <a class="btn small" href="${pageContext.request.contextPath}/admin/rooms?mode=edit&id=${r.roomId}">Edit</a>

                  <form action="${pageContext.request.contextPath}/admin/rooms" method="post" style="margin:0;">
                    <input type="hidden" name="mode" value="delete"/>
                    <input type="hidden" name="id" value="${r.roomId}"/>
                    <button class="btn small danger" type="submit"
                            onclick="return confirm('Delete this room?');">
                      Delete
                    </button>
                  </form>
                </div>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty requestScope.rooms}">
            <tr>
              <td colspan="6" style="color:#64748b; padding:16px;">
                No rooms have been created yet
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