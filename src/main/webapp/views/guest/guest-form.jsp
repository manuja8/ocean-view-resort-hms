<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Guest Form</title>
</head>
<body>

<h2>${guest != null ? "Edit Guest" : "Add Guest"}</h2>

<form action="guest" method="post">
    <input type="hidden" name="action" value="save"/>
    <input type="hidden" name="guestId" value="${guest.guestId}"/>

    Full Name: <input type="text" name="fullName" value="${guest.fullName}" required/><br/>
    Contact: <input type="text" name="contactNumber" value="${guest.contactNumber}" required/><br/>
    Email: <input type="email" name="email" value="${guest.email}" required/><br/>
    Address: <input type="text" name="address" value="${guest.address}" required/><br/>

    <button type="submit">Save</button>
</form>

<a href="guest?action=list">Back</a>

</body>
</html>