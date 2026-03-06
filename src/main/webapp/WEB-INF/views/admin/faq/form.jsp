<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="FAQ Form" />

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - FAQ Form</title>
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
              <c:when test="${param.mode == 'edit'}">Update FAQ</c:when>
              <c:otherwise>Add FAQ</c:otherwise>
            </c:choose>
          </h2>
          <div class="subtitle"></div>
        </div>

        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/admin/faq">Back</a>
        </div>
      </div>

      <div class="panel form-panel">
        <form action="${pageContext.request.contextPath}/admin/faq" method="post">
          <input type="hidden" name="mode" value="${param.mode}" />
          <input type="hidden" name="id" value="${param.id}" />

          <div class="form-grid">

            <div class="field full">
              <label>Question</label>
              <textarea class="textarea" name="question" placeholder="Type the question..." required>${requestScope.faq.question}</textarea>
            </div>

            <div class="field full">
              <label>Answer</label>
              <textarea class="textarea" name="answer" placeholder="Type the answer..." required>${requestScope.faq.answer}</textarea>
            </div>

            <div class="field">
              <label>Active</label>
              <select class="select" name="isActive">
                <option value="1" ${requestScope.faq.active ? 'selected' : ''}>Active</option>
                <option value="0" ${!requestScope.faq.active ? 'selected' : ''}>Inactive</option>
              </select>
            </div>

          </div>

          <div class="toolbar" style="margin-top:14px;">
            <button class="btn primary" type="submit">Save</button>
            <a class="btn" href="${pageContext.request.contextPath}/admin/faq">Cancel</a>
          </div>
        </form>
      </div>

    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>

</body>
</html>