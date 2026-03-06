<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Reports" />

<!DOCTYPE html>
<html>
<head>
  <title>OceanView HMS - Reports</title>
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
          <h2>Reports</h2>
          <div class="subtitle"></div>
        </div>
      </div>

      <div class="panel form-panel" style="margin-bottom:14px;">
        <form action="${pageContext.request.contextPath}/admin/reports" method="get">
          <div class="form-grid">

            <div class="field">
              <label>Report Type</label>
              <select class="select" name="type">
                <option value="occupancy" ${param.type=='occupancy'?'selected':''}>Room Management Report</option>
                <option value="revenue" ${param.type=='revenue'?'selected':''}>Revenue Report</option>
                <option value="payments" ${param.type=='payments'?'selected':''}>Payments Report</option>
              </select>
            </div>

            <div class="field">
              <label>From</label>
              <input class="input" type="date" name="from" value="${param.from}" />
            </div>

            <div class="field">
              <label>To</label>
              <input class="input" type="date" name="to" value="${param.to}" />
            </div>

            <div class="field button-field">
              <label>&nbsp;</label>
              <button class="btn primary" type="submit">Generate</button>
            </div>

          </div>
        </form>
      </div>

      <div class="panel">
        <table class="table">
          <thead>
          <tr>
            <th>Description</th>
            <th>Value</th>
          </tr>
          </thead>
          <tbody>

          <c:forEach var="row" items="${requestScope.reportRows}">
            <tr>
              <td>${row.metric}</td>
              <td>${row.value}</td>
            </tr>
          </c:forEach>

          <c:if test="${empty requestScope.reportRows}">
            <tr>
              <td colspan="2" style="color:#64748b; padding:16px;">
                No report generated yet. Select type and date range, then click Generate.
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