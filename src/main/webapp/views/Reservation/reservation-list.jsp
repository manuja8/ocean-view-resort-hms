<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.oceanview.dto.ReservationDTO" %>

<h2>Reservations</h2>

<a href="reservation?action=new">Add New Reservation</a>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Guest</th>
        <th>Room</th>
        <th>Check-In</th>
        <th>Check-Out</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

<%
    List<ReservationDTO> list =
        (List<ReservationDTO>) request.getAttribute("reservationList");

    if (list != null) {
        for (ReservationDTO r : list) {
%>
<tr>
    <td><%= r.getReservationNo() %></td>
    <td><%= r.getGuest().getGuestId() %></td>
    <td><%= r.getRoom().getRoomId() %></td>
    <td><%= r.getCheckIn() %></td>
    <td><%= r.getCheckOut() %></td>
    <td><%= r.getStatus() %></td>
    <td>
        <a href="reservation?action=cancel&id=<%= r.getReservationNo() %>">
            Cancel
        </a>
    </td>
</tr>
<%
        }
    }
%>
</table>