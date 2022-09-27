<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="">
                    <c:forEach var="person" items="${currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList}" varStatus="status">
                        <c:set var="oldPerson"
                               value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList[status.index]}"/>
                        <p class="col-xs-12">
                            <strong>
                                Key Appointment Holder<c:if
                                    test="${fn:length(currentPreviewSvcInfo.appSvcKeyAppointmentHolderDtoList)>1}">${status.index+1}</c:if>:
                            </strong>
                        </p>
                        <%@include file="viewPersonnelDetail.jsp" %>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>