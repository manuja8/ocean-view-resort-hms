<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Generate Bill</title>
</head>
<body>

<h2>Generate Bill</h2>

<form method="post" action="${pageContext.request.contextPath}/billing">
    <label>Reservation No:</label>
    <input type="number" name="reservationNo" required />
    <button type="submit">Generate Bill</button>
</form>

<br>
<a href="${pageContext.request.contextPath}/dashboard">Back to Dashboard</a>

</body>
</html>