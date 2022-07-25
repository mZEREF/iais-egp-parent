<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="app-title">${currStepName}</label>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach var="person" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
            <iais:row>
                <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                    <p><strong>Clinical Governance Officer<c:if test="${currentPreviewSvcInfo.appSvcCgoDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
                </div>
                <%@include file="viewPersonnelDetail.jsp"%>
            </iais:row>
        </c:forEach>
    </div>
</div>

