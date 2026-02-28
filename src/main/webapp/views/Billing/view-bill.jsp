<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.oceanview.dto.BillDTO" %>
<html>
<head>
    <title>View Bill</title>
</head>
<body>

<h2>Bill Details</h2>

<%
    BillDTO bill = (BillDTO) request.getAttribute("bill");

    if (bill != null) {
%>

    <p><strong>Bill No:</strong> <%= bill.getBillNo() %></p>
    <p><strong>Reservation No:</strong> <%= bill.getReservation().getReservationNo() %></p>

    <h3>Itemized Charges:</h3>
    <pre><%= bill.getItemizedCharges() %></pre>

    <p><strong>Total Amount:</strong> <%= bill.getTotalAmount() %></p>

<%
    } else {
%>
    <p>No bill found.</p>
<%
    }
%>

<br>
<a href="${pageContext.request.contextPath}/dashboard">Back to Dashboard</a>

</body>
</html>