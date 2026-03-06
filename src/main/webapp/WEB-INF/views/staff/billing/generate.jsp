<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Billing</title>
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
          <h2>Calculate Bill</h2>
          <div class="subtitle">Enter a Reservation ID and calculate the bill.</div>
        </div>
      </div>

      <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
       <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>


      <div class="panel form-panel">
        <form action="${pageContext.request.contextPath}/staff/billing" method="post">

          <div class="form-grid">
            <div class="field">
              <label>Reservation ID</label>
              <input class="input" type="number" name="reservationId" placeholder="e.g., 12" required />
            </div>

            <div class="field">
              <label>Discount (LKR)</label>
              <input class="input" type="number" step="0.01" min="0" name="discount" placeholder="0.00" />
            </div>

            <div class="field">
              <label>Tax (%)</label>
              <input class="input" type="number" step="0.01" min="0" name="taxPercent" placeholder="0" />
            </div>

            <div class="field">
              <label>Recalculate?</label>
              <div style="display:flex; align-items:center; gap:10px; height:42px;">
                <input type="checkbox" name="force" />
                <span class="subtitle">Cancel old bill & create new</span>
              </div>
            </div>
          </div>

          <div class="toolbar" style="margin-top:14px;">
            <button class="btn primary" type="submit">Calculate</button>
            <a class="btn" href="${pageContext.request.contextPath}/dashboard">Back</a>
          </div>

        </form>
      </div>
    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>
</body>
</html>