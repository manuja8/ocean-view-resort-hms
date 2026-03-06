<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:set var="pageTitle" value="User Accounts" />

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - User Accounts</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />

    <script>
      // format date to YYYY-MM-DD
      function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-CA');
      }
    </script>

</head>
<body>

<div class="app">
  <%@ include file="/WEB-INF/views/layouts/sidebar.jspf" %>

  <div class="main">
    <%@ include file="/WEB-INF/views/layouts/topbar.jspf" %>

    <div class="content">

      <div class="pagebar">
        <div>
          <h2>User Accounts</h2>
          <div class="subtitle"></div>
        </div>

       <div class="toolbar">
         <form action="${pageContext.request.contextPath}/admin/users" method="get">
           <input class="input" type="text" name="q"
                  placeholder="Search users..." value="${param.q}" />

           <button class="btn" type="submit">Search</button>


           <a class="btn" href="${pageContext.request.contextPath}/admin/users">Clear</a>
         </form>

         <a class="btn primary" href="${pageContext.request.contextPath}/admin/users?mode=create">Create Account</a>
       </div>
      </div>

      <c:if test="${not empty success}">
        <div class="alert success">${success}</div>
      </c:if>
      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="panel">
        <table class="table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Full Name</th>
            <th>Contact</th>
            <th>Address</th>
            <th>Role</th>
            <th>Status</th>
            <th>Blocked</th>
            <th>Expiry</th>
            <th>Last Login</th>
            <th>Actions</th>
          </tr>
          </thead>

          <tbody>
          <!-- Expected: requestScope.users = List<UserDTO/Model> -->
          <c:forEach var="u" items="${requestScope.users}">
            <tr>
              <td>${u.userId}</td>
              <td>${u.username}</td>
              <td>${u.fullName}</td>
              <td>${u.contactNo}</td>
              <td>${u.address}</td>
              <td><span class="pill"><span class="dot"></span>${u.roleName}</span></td>

              <td>
                <c:choose>
                  <c:when test="${u.active}">
                    <span class="pill ok"><span class="dot"></span>Active</span>
                  </c:when>
                  <c:otherwise>
                    <span class="pill danger"><span class="dot"></span>Inactive</span>
                  </c:otherwise>
                </c:choose>
              </td>

              <td>
                <c:choose>
                  <c:when test="${u.blocked}">
                    <span class="pill warn"><span class="dot"></span>Blocked</span>
                  </c:when>
                  <c:otherwise>
                    <span class="pill ok"><span class="dot"></span>No</span>
                  </c:otherwise>
                </c:choose>
              </td>

                 <td><script>document.write(formatDate("${u.expiryDate}"))</script></td>
                 <td><script>document.write(formatDate("${u.lastLogin}"))</script></td>

              <td>
                <div class="toolbar">
                  <a class="btn small" href="${pageContext.request.contextPath}/admin/users?mode=edit&id=${u.userId}">Edit</a>

                  <!-- Delete should be POST in backend later -->
                  <form action="${pageContext.request.contextPath}/admin/users" method="post" style="margin:0;">
                    <input type="hidden" name="mode" value="delete"/>
                    <input type="hidden" name="id" value="${u.userId}"/>
                    <button class="btn small danger" type="submit"
                            onclick="return confirm('Delete this user account?');">
                      Delete
                    </button>
                  </form>
                </div>
              </td>
            </tr>
          </c:forEach>

          <!-- Empty state -->
          <c:if test="${empty requestScope.users}">
            <tr>
              <td colspan="10" style="color:#64748b; padding:16px;">
                No users loaded yet. (Backend will provide users list)
              </td>
            </tr>
          </c:if>

          </tbody>
        </table>
      </div>

    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>

</body>
</html>