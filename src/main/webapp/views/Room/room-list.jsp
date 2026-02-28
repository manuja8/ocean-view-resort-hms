<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Room List</title>
</head>
<body>

<h2>Rooms</h2>

<a href="room?action=edit">Add New Room</a>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Number</th>
        <th>Type</th>
        <th>Price</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

    <c:forEach var="room" items="${roomList}">
        <tr>
            <td>${room.roomId}</td>
            <td>${room.roomNumber}</td>
            <td>${room.roomType}</td>
            <td>${room.pricePerNight}</td>
            <td>${room.status}</td>
            <td>
                <a href="room?action=edit&id=${room.roomId}">Edit</a>
                <form action="room" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete"/>
                    <input type="hidden" name="id" value="${room.roomId}"/>
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>

</table>

</body>
</html>