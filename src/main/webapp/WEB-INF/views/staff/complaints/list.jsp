<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Complaints</title>
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
          <h2>Complaints</h2>
          <div class="subtitle">Log and track guest complaints.</div>
        </div>

        <div class="toolbar">
          <form action="${pageContext.request.contextPath}/staff/complaints" method="get">
            <input class="input" type="text" name="q" placeholder="Search..." value="${param.q}" />

            <select class="select" name="status">
              <option value="">All Status</option>
              <option value="open" ${param.status=='open'?'selected':''}>Open</option>
              <option value="in_progress" ${param.status=='in_progress'?'selected':''}>In Progress</option>
              <option value="resolved" ${param.status=='resolved'?'selected':''}>Resolved</option>
              <option value="closed" ${param.status=='closed'?'selected':''}>Closed</option>
            </select>

            <select class="select" name="priority">
              <option value="">All Priority</option>
              <option value="low" ${param.priority=='low'?'selected':''}>Low</option>
              <option value="medium" ${param.priority=='medium'?'selected':''}>Medium</option>
              <option value="high" ${param.priority=='high'?'selected':''}>High</option>
            </select>

            <button class="btn" type="submit">Search</button>
            <a class="btn" href="${pageContext.request.contextPath}/staff/complaints">Clear</a>
          </form>

          <a class="btn primary" href="${pageContext.request.contextPath}/staff/complaints?mode=create">Add Complaint</a>
        </div>
      </div>

      <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
      <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>

      <div class="panel">
        <table class="table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Guest</th>
            <th>Reservation</th>
            <th>Subject</th>
            <th>Priority</th>
            <th>Status</th>
            <th>Date</th>
            <th style="width:220px;">Actions</th>
          </tr>
          </thead>
          <tbody>

          <c:forEach var="c" items="${complaints}">
            <tr>
              <td style="font-weight:900; color:#0f172a;">${c.complaintId}</td>
              <td>${c.guestName} (${c.guestContact})</td>
              <td><c:out value="${c.reservationNumber}" default="—"/></td>
              <td>${c.subject}</td>
              <td>${c.priority}</td>
              <td>${c.status}</td>
              <td><c:out value="${c.createdAt}" default="—"/></td>
              <td>
                <a class="btn small" href="${pageContext.request.contextPath}/staff/complaints?mode=edit&id=${c.complaintId}">Edit</a>

                <c:if test="${sessionScope.role == 'admin'}">
                  <form action="${pageContext.request.contextPath}/admin/complaints" method="post" style="display:inline;">
                    <input type="hidden" name="mode" value="delete"/>
                    <input type="hidden" name="id" value="${c.complaintId}"/>
                    <button class="btn small danger" type="submit"
                            onclick="return confirm('Delete this complaint?');">
                      Delete
                    </button>
                  </form>
                </c:if>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty complaints}">
            <tr><td colspan="8" style="color:#64748b; padding:16px;">No complaints found.</td></tr>
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