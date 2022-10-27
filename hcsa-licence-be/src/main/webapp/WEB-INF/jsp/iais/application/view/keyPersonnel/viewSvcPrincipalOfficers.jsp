<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:set var="officeTelNo" value="officeTelNo"/>
        <c:forEach var="person" items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" varStatus="status">
            <iais:row>
                <div  class="col-xs-12">
                    <p><strong>Principal Officer<c:if test="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewPersonnelDetail.jsp"%>
        </c:forEach>

        <c:forEach var="person" items="${currentPreviewSvcInfo.appSvcNomineeDtoList}" varStatus="status">
            <iais:row>
                <div  class="col-xs-12">
                    <p><strong>Nominee<c:if test="${currentPreviewSvcInfo.appSvcNomineeDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewPersonnelDetail.jsp"%>
        </c:forEach>
    </div>
</div>
