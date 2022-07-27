<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
  <label class="app-title">${currStepName}</label>
  <div class="amend-preview-info">
    <c:forEach items="${currentPreviewSvcInfo.appSvcMedAlertPersonList}" var="appSvcMedAlertPerson" varStatus="status">
      <iais:row>
        <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
          <p><strong>MedAlert Person<c:if test="${currentPreviewSvcInfo.appSvcMedAlertPersonList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
        </div>
        <%@include file="viewMedAlertDetail.jsp"%>
      </iais:row>
    </c:forEach>
  </div>
</div>

