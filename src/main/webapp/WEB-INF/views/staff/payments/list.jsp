<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Payments</title>
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
          <h2>Payments</h2>
          <div class="subtitle">Process and view payments </div>
        </div>

        <div class="toolbar">
          <form action="${pageContext.request.contextPath}/staff/payments" method="get">
            <input class="input" type="text" name="q" placeholder="Bill ID or reference..." value="${param.q}" />

            <select class="select" name="statusId">
              <option value="">All Status</option>
              <c:forEach var="s" items="${statuses}">
                <option value="${s.id}" ${param.statusId==''+s.id?'selected':''}>${s.name}</option>
              </c:forEach>
            </select>

            <button class="btn" type="submit">Search</button>
            <a class="btn" href="${pageContext.request.contextPath}/staff/payments">Clear</a>
          </form>

          <a class="btn primary" href="${pageContext.request.contextPath}/staff/payments?mode=create">Process Payment</a>
        </div>
      </div>

      <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
      <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>

      <div class="panel">
        <table class="table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Bill</th>
            <th>Amount</th>
            <th>Method</th>
            <th>Status</th>
            <th>Reference</th>
            <th>Date</th>
          </tr>
          </thead>
          <tbody>

          <c:forEach var="p" items="${payments}">
            <tr>
              <td style="font-weight:900; color:#0f172a;">${p.paymentId}</td>
              <td>${p.billId}</td>
              <td>LKR ${p.amount}</td>
              <td>${p.paymentMethodName}</td>
              <td>${p.paymentStatusName}</td>
              <td><c:out value="${p.paymentReference}" default="—"/></td>
              <td><c:out value="${p.paymentDate}" default="—"/></td>
            </tr>
          </c:forEach>

          <c:if test="${empty payments}">
            <tr><td colspan="7" style="color:#64748b; padding:16px;">No payments found.</td></tr>
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