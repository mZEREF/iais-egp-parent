<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:set var="keyPerson" value="keyPerson"/>
        <c:forEach var="person" items="${currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList}" varStatus="status">
            <iais:row>
                <div  class="col-xs-12">
                    <p><strong>Key Appointment Holder<c:if test="${currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong></p>
                </div>
            </iais:row>
            <c:set var="keyPerson" value="keyPerson"/>
            <%@include file="viewPersonnelDetail.jsp"%>
        </c:forEach>
    </div>
</div>


