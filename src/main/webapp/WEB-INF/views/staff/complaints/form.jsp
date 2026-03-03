<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Complaint Form</title>
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
          <h2><c:choose><c:when test="${mode=='edit'}">Update Complaint</c:when><c:otherwise>Add Complaint</c:otherwise></c:choose></h2>
          <div class="subtitle">Log complaint details.</div>
        </div>
        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/staff/complaints">Back</a>
        </div>
      </div>

      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="panel form-panel">
        <form action="${pageContext.request.contextPath}/staff/complaints" method="post">
          <input type="hidden" name="mode" value="${mode}" />
          <c:if test="${mode=='edit'}">
            <input type="hidden" name="id" value="${complaint.complaintId}" />
          </c:if>

          <div class="form-grid">

            <div class="field">
              <label>Guest</label>
              <select class="select" name="guestId" required>
                <option value="">-- Select Guest --</option>
                <c:forEach var="g" items="${guests}">
                  <option value="${g.guestId}" ${complaint.guestId==g.guestId?'selected':''}>
                    ${g.fullName} (${g.contactNo})
                  </option>
                </c:forEach>
              </select>
            </div>

            <div class="field">
              <label>Reservation ID (optional)</label>
              <input class="input" type="number" name="reservationId" value="${complaint.reservationId}" placeholder="Leave blank if not applicable" />
            </div>

            <div class="field full">
              <label>Subject</label>
              <input class="input" type="text" name="subject" value="${complaint.subject}" required />
            </div>

            <div class="field full">
              <label>Description</label>
              <textarea class="textarea" name="description" required>${complaint.description}</textarea>
            </div>

            <div class="field">
              <label>Priority</label>
              <select class="select" name="priority" required>
                <option value="low" ${complaint.priority=='low'?'selected':''}>Low</option>
                <option value="medium" ${complaint.priority=='medium'?'selected':''}>Medium</option>
                <option value="high" ${complaint.priority=='high'?'selected':''}>High</option>
              </select>
            </div>

            <div class="field">
              <label>Status</label>
              <select class="select" name="status" required>
                <option value="open" ${complaint.status=='open'?'selected':''}>Open</option>
                <option value="in_progress" ${complaint.status=='in_progress'?'selected':''}>In Progress</option>
                <option value="resolved" ${complaint.status=='resolved'?'selected':''}>Resolved</option>
                <option value="closed" ${complaint.status=='closed'?'selected':''}>Closed</option>
              </select>
            </div>

          </div>

          <div class="toolbar" style="margin-top:14px;">
            <button class="btn primary" type="submit">Save</button>
            <a class="btn" href="${pageContext.request.contextPath}/staff/complaints">Cancel</a>
          </div>
        </form>
      </div>
    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>
</body>
</html>