<h2>Create Reservation</h2>

<form action="reservation" method="post">

    Guest ID:
    <input type="number" name="guestId" required/><br/><br/>

    Room ID:
    <input type="number" name="roomId" required/><br/><br/>

    Check In:
    <input type="date" name="checkIn" required/><br/><br/>

    Check Out:
    <input type="date" name="checkOut" required/><br/><br/>

    <button type="submit">Save</button>

</form>