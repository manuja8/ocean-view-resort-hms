<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Notifications</title>
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
          <h2>Notifications</h2>
          <div class="subtitle">System updates from Payments and Complaints.</div>
        </div>
        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/dashboard">Back</a>
        </div>
      </div>

      <div class="panel">
        <c:if test="${empty notifications}">
          <div style="color:#64748b;">No notifications yet.</div>
        </c:if>

        <c:forEach var="n" items="${notifications}">
          <div style="padding:12px; border:1px solid #e6e9f2; border-radius:14px; background:#fff; margin-bottom:10px;">
            <div style="display:flex; justify-content:space-between; gap:12px; flex-wrap:wrap;">
              <div style="font-weight:900; color:#0f172a;">${n.title}</div>
              <div style="color:#64748b; font-size:0.9rem;">${n.createdAt}</div>
            </div>
            <div style="color:#334155; margin-top:6px;">${n.body}</div>
          </div>
        </c:forEach>
      </div>

    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>
</body>
</html>