<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>OceanView HMS - Login</title>

  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/login.css" />
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@600;700&display=swap" rel="stylesheet" />
</head>

<body data-context="<%=request.getContextPath()%>">
  <div class="homepg-grid-container">

    <div class="homepg-auth-panel">
      <h1 class="homepg-auth-title">
        <span class="homepg-auth-welcome">Welcome</span> OceanView Resort
      </h1>

      <div class="homepg-form-container">
        <div class="homepg-form-box">
          <h2 class="homepg-form-heading">Login</h2>

          <%
            String err = (String) request.getAttribute("error");
            if (err != null) {
          %>
              <p class="homepg-message"><%= err %></p>
          <%
            }
          %>

          <form action="<%=request.getContextPath()%>/login" method="post">
            <input type="text" name="username" placeholder="Username" required />
            <input type="password" name="password" placeholder="Password" required />
            <button type="submit">Login</button>
          </form>

        </div>
      </div>
    </div>

    <div id="slideshow" class="homepg-slideshow"></div>

  </div>

  <script src="<%=request.getContextPath()%>/assets/js/login-slideshow.js"></script>
</body>
</html>