<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="FAQ Management" />

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - FAQ Management</title>
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
          <h2>FAQ Management</h2>
          <div class="subtitle">Admin-only: add, update, delete FAQs.</div>
        </div>

        <div class="toolbar">
          <a class="btn primary" href="${pageContext.request.contextPath}/admin/faq?mode=create">Add FAQ</a>
        </div>
      </div>

      <div class="panel">
        <table class="table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Question</th>
            <th>Answer</th>
            <th>Active</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>

          <c:forEach var="f" items="${requestScope.faqList}">
            <tr>
              <td>${f.faqId}</td>
              <td>${f.question}</td>
              <td>${f.answer}</td>
              <td>
                <c:choose>
                  <c:when test="${f.active}">
                    <span class="pill ok"><span class="dot"></span>Active</span>
                  </c:when>
                  <c:otherwise>
                    <span class="pill danger"><span class="dot"></span>Inactive</span>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <div class="toolbar">
                  <a class="btn small" href="${pageContext.request.contextPath}/admin/faq?mode=edit&id=${f.faqId}">Edit</a>

                  <form action="${pageContext.request.contextPath}/admin/faq" method="post" style="margin:0;">
                    <input type="hidden" name="mode" value="delete"/>
                    <input type="hidden" name="id" value="${f.faqId}"/>
                    <button class="btn small danger" type="submit"
                            onclick="return confirm('Delete this FAQ?');">
                      Delete
                    </button>
                  </form>
                </div>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty requestScope.faqList}">
            <tr>
              <td colspan="4" style="color:#64748b; padding:16px;">
                No FAQs has been created yet
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