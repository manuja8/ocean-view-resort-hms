<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Guest Form</title>
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
          <h2><c:choose><c:when test="${mode=='edit'}">Update Guest</c:when><c:otherwise>Add Guest</c:otherwise></c:choose></h2>
          <div class="subtitle">Guest details stored in the system.</div>
        </div>
        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/staff/guests">Back</a>
        </div>
      </div>

      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="panel form-panel">
        <form action="${pageContext.request.contextPath}/staff/guests" method="post">
          <input type="hidden" name="mode" value="${mode}" />
          <c:if test="${mode=='edit'}">
            <input type="hidden" name="id" value="${guest.guestId}" />
          </c:if>

          <div class="form-grid">

            <div class="field full">
              <label>Full Name</label>
              <input class="input" type="text" name="fullName" value="${guest.fullName}" required />
            </div>

            <div class="field">
              <label>Gender</label>
              <select class="select" name="gender">
                <option value="">-- Select --</option>
                <option value="Male" ${guest.gender=='Male'?'selected':''}>Male</option>
                <option value="Female" ${guest.gender=='Female'?'selected':''}>Female</option>
              </select>
            </div>

            <div class="field">
              <label>Date of Birth</label>
              <input class="input" type="date" name="dateOfBirth" value="${guest.dateOfBirth}" />
            </div>

            <div class="field full">
              <label>Address</label>
              <input class="input" type="text" name="address" value="${guest.address}" required />
            </div>

            <div class="field">
              <label>Contact No</label>
              <input class="input" type="text" name="contactNo" value="${guest.contactNo}" required />
            </div>

            <div class="field">
              <label>Email</label>
              <input class="input" type="email" name="email" value="${guest.email}" />
            </div>

            <div class="field">
              <label>Identification Type</label>
              <select class="select" name="identificationType" required>
                <option value="">-- Select --</option>
                <option value="NIC" ${guest.identificationType=='NIC'?'selected':''}>NIC</option>
                <option value="Passport" ${guest.identificationType=='Passport'?'selected':''}>Passport</option>
                <option value="Driver License" ${guest.identificationType=='Driver License'?'selected':''}>Driver License</option>
              </select>
            </div>

            <div class="field">
              <label>Identification No</label>
              <input class="input" type="text" name="identificationNo" value="${guest.identificationNo}" required />
            </div>

          </div>

          <div class="toolbar" style="margin-top:14px;">
            <button class="btn primary" type="submit">Save</button>
            <a class="btn" href="${pageContext.request.contextPath}/staff/guests">Cancel</a>
          </div>
        </form>
      </div>

    </div>
    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>
</body>
</html>