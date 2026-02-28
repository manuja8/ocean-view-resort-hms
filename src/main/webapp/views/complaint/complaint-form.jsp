<form method="post" action="${pageContext.request.contextPath}/complaints">

    Subject:
    <input type="text" name="subject" required />

    Description:
    <textarea name="description" required></textarea>

    Priority:
    <select name="priority">
        <option value="low">Low</option>
        <option value="medium">Medium</option>
        <option value="high">High</option>
    </select>

    <button type="submit">Submit Complaint</button>

</form>