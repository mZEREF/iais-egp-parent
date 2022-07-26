<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach var="person" items="${currentPreviewSvcInfo.appSvcCgoDtoList}" varStatus="status">
            <iais:row>
                <p><strong>Clinical Governance Officer<c:if test="${currentPreviewSvcInfo.appSvcCgoDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
            </iais:row>
            <%@include file="viewPersonnelDetail.jsp"%>
        </c:forEach>
    </div>
</div>

