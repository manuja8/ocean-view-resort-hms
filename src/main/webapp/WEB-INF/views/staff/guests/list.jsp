<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Guests</title>
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
          <h2>Guests</h2>
          <div class="subtitle"></div>
        </div>

        <div class="toolbar">
          <form action="${pageContext.request.contextPath}/staff/guests" method="get">
            <input class="input" type="text" name="q" placeholder="Search guest..." value="${param.q}" />
            <button class="btn" type="submit">Search</button>
            <a class="btn" href="${pageContext.request.contextPath}/staff/guests">Clear</a>
          </form>

          <a class="btn primary" href="${pageContext.request.contextPath}/staff/guests?mode=create">Add Guest</a>
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
            <th>Full Name</th>
            <th>Contact</th>
            <th>ID Type</th>
            <th>ID No</th>
            <th>Email</th>
            <th style="width:160px;">Actions</th>
          </tr>
          </thead>
          <tbody>

          <c:forEach var="g" items="${guestList}">
            <tr>
              <td style="font-weight:900; color:#0f172a;">${g.fullName}</td>
              <td>${g.contactNo}</td>
              <td>${g.identificationType}</td>
              <td>${g.identificationNo}</td>
              <td><c:out value="${g.email}" default="—"/></td>
              <td>
                <a class="btn small" href="${pageContext.request.contextPath}/staff/guests?mode=edit&id=${g.guestId}">Edit</a>

                <!-- Admin-only delete button -->
                <c:if test="${sessionScope.role == 'admin'}">
                  <form action="${pageContext.request.contextPath}/admin/guests" method="post" style="display:inline;">
                    <input type="hidden" name="mode" value="delete"/>
                    <input type="hidden" name="id" value="${g.guestId}"/>
                    <button class="btn small danger" type="submit"
                            onclick="return confirm('Delete this guest? This may fail if reservations exist.');">
                      Delete
                    </button>
                  </form>
                </c:if>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty guestList}">
            <tr><td colspan="6" style="color:#64748b; padding:16px;">No guests found.</td></tr>
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