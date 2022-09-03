<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <c:set var="appSvcOtherInfoList" value="${currentPreviewSvcInfo.appSvcOtherInfoList}"/>
    <c:forEach var="appSvcOtherInfoDto" items="${appSvcOtherInfoList}">
        <div class="amend-preview-info form-horizontal min-row">
            <%@include file="viewDentalService.jsp"%>
            <%@include file="viewRenalDialysisCentreService.jsp"%>
            <%@include file="viewAmbulatorySurgicalCentreService.jsp"%>
            <%@include file="viewOtherInformationTopPerson.jsp"%>
            <%@include file="viewOtherForm.jsp"%>
            <%@include file="viewDoucmentation.jsp"%>
            <%@include file="viewAbort.jsp"%>
            <%@include file="viewYfVs.jsp"%>
        </div>
    </c:forEach>
</div>

