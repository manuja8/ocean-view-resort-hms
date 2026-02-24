<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Guest List</title>
</head>
<body>

<h2>Guest List</h2>

<a href="guest?action=edit">Add New Guest</a>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Contact</th>
        <th>Email</th>
        <th>Action</th>
    </tr>

    <c:forEach var="guest" items="${guestList}">
        <tr>
            <td>${guest.guestId}</td>
            <td>${guest.fullName}</td>
            <td>${guest.contactNumber}</td>
            <td>${guest.email}</td>
            <td>
                <a href="guest?action=edit&id=${guest.guestId}">Edit</a>
                <form action="guest" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete"/>
                    <input type="hidden" name="id" value="${guest.guestId}"/>
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>

</table>

</body>
</html>