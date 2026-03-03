<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Help (FAQ)</title>
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
          <h2>Help (FAQ)</h2>
          <div class="subtitle">Quick answers for common questions.</div>
        </div>

        <div class="toolbar">
          <a class="btn" href="${pageContext.request.contextPath}/dashboard">Back</a>
        </div>
      </div>

      <div class="panel">
        <c:if test="${empty faqList}">
          <div style="color:#64748b;">No FAQ entries found.</div>
        </c:if>

        <c:forEach var="f" items="${faqList}">
          <div style="border:1px solid #e6e9f2; border-radius:14px; padding:14px; margin-bottom:12px; background:#fff;">
            <div style="font-weight:900; color:#0f172a; margin-bottom:6px;">
              Q: ${f.question}
            </div>
            <div style="color:#334155; line-height:1.5;">
              A: ${f.answer}
            </div>
          </div>
        </c:forEach>
      </div>
    </div>

    <%@ include file="/WEB-INF/views/layouts/footer.jspf" %>
  </div>
</div>
</body>
</html>