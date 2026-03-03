<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>Print Bill</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />
</head>
<body>

<div style="max-width:900px; margin:24px auto; background:#fff; border:1px solid #e6e9f2; border-radius:14px; padding:18px;">
  <h2 style="margin:0 0 6px 0;">Ocean View Resort - Bill</h2>
  <div style="color:#64748b; margin-bottom:12px;">Bill ID: #${bill.billNo}</div>

  <div style="margin-bottom:10px;">
    <b>Status:</b>
    <c:choose>
      <c:when test="${isPaid}">PAID</c:when>
      <c:otherwise>UNPAID</c:otherwise>
    </c:choose>
    &nbsp; | &nbsp;
    <b>Paid:</b> LKR ${paidAmount}
    &nbsp; | &nbsp;
    <b>Remaining:</b> LKR ${remainingAmount}
  </div>

  <div style="display:flex; gap:18px; flex-wrap:wrap; margin-bottom:10px;">
    <div><b>Reservation:</b> ${bill.reservation.reservationNo}</div>
    <div><b>Nights:</b> ${bill.numNights}</div>
    <div><b>Total:</b> LKR ${bill.totalAmount}</div>
  </div>

  <hr style="border:none; border-top:1px solid #e6e9f2; margin:14px 0;">

  <pre style="white-space:pre-wrap; font-family:inherit; margin:0;">${bill.itemizedCharges}</pre>

  <div style="margin-top:16px; color:#64748b; font-size:0.9rem;">
    © 2026 Ocean View Resort. All rights reserved.
  </div>
</div>

<script>
  window.onload = function () { window.print(); };
</script>
</body>
</html>