<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="User Account Form" />

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - User Form</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />
</head>
<body>

<div class="app">
  <%@ include file="/WEB-INF/views/layouts/sidebar.jspf" %>

  <div class="main">
    <%@ include file="/WEB-INF/views/layouts/topbar.jspf" %>

    <div class="content">

      <div class="pagebar">
        <div>
          <h2>
            <c:choose>
              <c:when test="${param.mode == 'edit'}">Update Account</c:when>
              <c:otherwise>Create Account</c:otherwise>
            </c:choose>
          </h2>
          <div class="subtitle">Admin-only account management.</div>
        </div>

        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/admin/users">Back</a>
        </div>
      </div>

      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="panel form-panel">
        <form action="${pageContext.request.contextPath}/admin/users" method="post">
          <input type="hidden" name="mode" value="${param.mode}" />
          <input type="hidden" name="id" value="${param.id}" />

          <div class="form-grid">

            <div class="field">
              <label>Username</label>
              <input class="input" type="text" name="username" value="${requestScope.user.username}" required />
            </div>

            <div class="field">
              <label>Role</label>
              <select class="select" name="roleId" required>
                <option value="">-- Select Role --</option>
                <c:forEach var="r" items="${requestScope.roles}">
                  <option value="${r.roleId}" ${r.roleId == requestScope.user.roleId ? 'selected' : ''}>
                    ${r.roleName}
                  </option>
                </c:forEach>
              </select>
              <c:if test="${empty requestScope.roles}">
                <div class="help">Roles will load from DB (user_roles table).</div>
              </c:if>
            </div>

            <div class="field full">
              <label>Full Name</label>
              <input class="input" type="text" name="fullName" value="${requestScope.user.fullName}" required />
            </div>

            <div class="field full">
              <label>Address</label>
              <input class="input" type="text" name="address" value="${requestScope.user.address}" required />
            </div>

            <div class="field">
              <label>Contact No</label>
              <input class="input" type="text" name="contactNo" value="${requestScope.user.contactNo}" required />
            </div>

            <div class="field">
              <label>Expiry Date</label>
              <input class="input" type="datetime-local" name="expiryDate" value="${expiryDateValue}" />
            </div>

            <div class="field">
              <label>Password <c:if test="${param.mode == 'edit'}">(leave blank to keep)</c:if></label>
              <input class="input" type="password" name="password" placeholder="Enter password..." />
            </div>

            <div class="field">
              <label>Status</label>
              <select class="select" name="isActive">
                <option value="1" ${requestScope.user.active ? 'selected' : ''}>Active</option>
                <option value="0" ${!requestScope.user.active ? 'selected' : ''}>Inactive</option>
              </select>
            </div>

            <div class="field">
              <label>Blocked</label>
              <select class="select" name="isBlocked">
                <option value="0" ${!requestScope.user.blocked ? 'selected' : ''}>No</option>
                <option value="1" ${requestScope.user.blocked ? 'selected' : ''}>Yes</option>
              </select>
            </div>

          </div>

          <div class="toolbar" style="margin-top:14px;">
            <button class="btn primary" type="submit">Save</button>
            <a class="btn" href="${pageContext.request.contextPath}/admin/users">Cancel</a>
          </div>
        </form>
      </div>

    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>

</body>
</html>