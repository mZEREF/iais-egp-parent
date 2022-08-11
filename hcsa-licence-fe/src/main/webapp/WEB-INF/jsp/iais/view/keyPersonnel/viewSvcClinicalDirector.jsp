<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<iais:row>
    <label class="app-title">${currStepName}</label>
</iais:row>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title hidden">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach var="person" items="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList}" varStatus="status">
            <iais:row>
                <div class="col-xs-12">
                    <p><strong>Clinical Governance Officer<c:if test="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewClinicalDirectorDetail.jsp"%>
        </c:forEach>
    </div>
</div>

