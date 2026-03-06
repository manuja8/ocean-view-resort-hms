<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Bill</title>
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
          <h2>Bill</h2>
          <div class="subtitle">Bill details and printable view.</div>
        </div>
        <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
               <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>
        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/staff/billing">Back</a>
        </div>
      </div>

      <c:if test="${bill == null}">
        <div class="alert error">No active bill found for this reservation.</div>
      </c:if>

      <c:if test="${bill != null}">
        <div class="panel">
          <div style="display:flex; justify-content:space-between; gap:12px; flex-wrap:wrap;">
            <div>
              <div class="subtitle">Bill ID</div>
              <div style="font-weight:900; font-size:1.1rem;">#${bill.billNo}</div>
            </div>
            <div>
              <div class="subtitle">Reservation ID</div>
              <div style="font-weight:900; font-size:1.1rem;">${bill.reservation.reservationNo}</div>
            </div>
            <div>
              <div class="subtitle">Nights</div>
              <div style="font-weight:900; font-size:1.1rem;">${bill.numNights}</div>
            </div>
            <div>
              <div class="subtitle">Total</div>
              <div style="font-weight:900; font-size:1.1rem;">LKR ${bill.totalAmount}</div>
            </div>
          </div>

          <!-- Payment status -->
          <div style="display:flex; gap:12px; flex-wrap:wrap; margin-top:12px;">
            <c:choose>
              <c:when test="${isPaid}">
                <span class="pill ok"><span class="dot ok"></span>PAID</span>
              </c:when>
              <c:otherwise>
                <span class="pill warn"><span class="dot warn"></span>UNPAID</span>
              </c:otherwise>
            </c:choose>

            <span class="pill"><span class="dot"></span>Paid: LKR ${paidAmount}</span>
            <span class="pill"><span class="dot"></span>Remaining: LKR ${remainingAmount}</span>
          </div>

          <hr style="border:none; border-top:1px solid #e6e9f2; margin:14px 0;">

          <div class="subtitle" style="margin-bottom:8px;">Breakdown</div>
          <pre style="margin:0; white-space:pre-wrap; font-family:inherit; color:#0f172a;">${bill.itemizedCharges}</pre>

          <div class="toolbar" style="margin-top:14px;">
            <a class="btn primary"
               href="${pageContext.request.contextPath}/staff/billing?mode=print&billId=${bill.billNo}">
              Print Bill
            </a>

            <c:if test="${not isPaid}">
              <a class="btn"
                 href="${pageContext.request.contextPath}/staff/payments?mode=create&billId=${bill.billNo}">
                Process Payment
              </a>
            </c:if>
          </div>
        </div>
      </c:if>
    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>
</body>
</html>