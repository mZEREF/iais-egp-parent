<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="">
                    <c:choose>
                    <c:when test="${currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL || currentPreviewSvcInfo.serviceCode == AppServicesConsts.SERVICE_CODE_BLOOD_BANKING}">
                        <%@include file="viewClinicalBoratoryContent.jsp"%>
                        <%@include file="viewRadiologicalServiceContent.jsp"%>
                    </c:when>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>