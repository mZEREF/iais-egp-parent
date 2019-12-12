<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/12/2019
  Time: 4:18 PM
  To change this template use File | Settings | File Templates.
--%>

<c:forEach var = "item" items="${checklistConfigAttr}" varStatus="status">
  <c:if test="${item.common eq true}">
    <div class="bg-title">
      <h2>General Regulation</h2>
    </div>
  </c:if>

  <c:if test="${item.common eq false}">
    <c:choose>
      <c:when test="${empty item.svcSubType}">
        <h2>${item.svcCode}</h2>
      </c:when>
      <c:otherwise>
        <h2>${item.svcCode} | ${item.svcSubType}</h2>
      </c:otherwise>
    </c:choose>
  </c:if>

  <!-- Content goes here -->
  <c:forEach var = "sec" items="${item.sectionDtos}">
    <p>
      <div class="panel panel-default">
        <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
          <div class="panel-body">
            <div class="panel-main-content">
              <div class="preview-info">
    <p>Section: &nbsp;<b>${sec.section}</b></p>
    <p>Description: &nbsp;${sec.description}</p>

    <table class="table">
      <thead>
      <tr>
        <th>Regulation Clause Number</th>
        <th>Regulations</th>
        <th>Checklist Item</th>
        <th>Risk Level</th>
      </tr>
      </thead>
      <tbody>
        <c:forEach var = "chklitem" items = "${sec.checklistItemDtos}" varStatus="status">
          <tr>
            <td>
              <p>${chklitem.regulationClauseNo}</p>
            </td>
            <td>
              <p>${chklitem.regulationClause}</p>
            </td>
            <td>
              <p>${chklitem.checklistItem}</p>
            </td>
            <td>
              <p>${chklitem.riskLevel}</p>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      </div>
      </div>
      </div>
      </div>
      </div>
      </p>
    </c:forEach>
</c:forEach>
