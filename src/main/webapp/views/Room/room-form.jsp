<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Room Form</title>
</head>
<body>

<h2>${room != null ? "Edit Room" : "Add Room"}</h2>

<form action="room" method="post">

    <input type="hidden" name="action" value="save"/>
    <input type="hidden" name="roomId" value="${room.roomId}"/>

    Room Number:
    <input type="text" name="roomNumber"
           value="${room.roomNumber}" required/><br/>

    Room Type:
    <input type="text" name="roomType"
           value="${room.roomType}" required/><br/>

    Price Per Night:
    <input type="number" step="0.01" name="pricePerNight"
           value="${room.pricePerNight}" required/><br/>

    Status:
    <select name="status">
        <option value="AVAILABLE">AVAILABLE</option>
        <option value="OCCUPIED">OCCUPIED</option>
        <option value="MAINTENANCE">MAINTENANCE</option>
    </select>

    <br/><br/>
    <button type="submit">Save</button>
</form>

<a href="room?action=list">Back</a>

</body>
</html>