<form method="post" action="${pageContext.request.contextPath}/payment">
    Bill ID:
    <input type="number" name="billId" required />

    Payment Method ID:
    <input type="number" name="methodId" required />

    Reference:
    <input type="text" name="reference" />

    <button type="submit">Pay</button>
</form>

${error}