<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Process Payment</title>
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
          <h2>Process Payment</h2>
          <div class="subtitle">Record a payment for a bill.</div>
        </div>
        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/staff/payments">Back</a>
        </div>
      </div>

      <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
      <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>

      <div class="panel form-panel">
        <form action="${pageContext.request.contextPath}/staff/payments" method="post">
          <div class="form-grid">

            <div class="field">
              <label>Bill ID</label>
              <input class="input" type="number" name="billId" value="${payment.billId}" required />
            </div>

            <div class="field">
              <label>Amount (LKR)</label>
              <input class="input" type="number" step="0.01" min="0.01" name="amount" placeholder="0.00" required />
            </div>

            <div class="field">
              <label>Method</label>
              <select class="select" name="methodId" required>
                <option value="">-- Select --</option>
                <c:forEach var="m" items="${methods}">
                  <option value="${m.id}">${m.name}</option>
                </c:forEach>
              </select>
            </div>

            <div class="field">
              <label>Status</label>
              <select class="select" name="statusId" required>
                <option value="">-- Select --</option>
                <c:forEach var="s" items="${statuses}">
                  <option value="${s.id}">${s.name}</option>
                </c:forEach>
              </select>
            </div>

            <div class="field full">
              <label>Reference (optional)</label>
              <input class="input" type="text" name="reference" placeholder="Card auth / bank ref / note" />
            </div>

          </div>

          <div class="toolbar" style="margin-top:14px;">
            <button class="btn primary" type="submit">Save Payment</button>
            <a class="btn" href="${pageContext.request.contextPath}/staff/payments">Cancel</a>
          </div>
        </form>
      </div>

    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>
</body>
</html>