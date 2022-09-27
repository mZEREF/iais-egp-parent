<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="">
                    <c:forEach var="person" items="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList}" varStatus="status">
                        <c:set var="oldPerson" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList[status.index]}"/>
                        <p class="col-xs-12">
                            <strong>
                                Principal Officer<c:if
                                    test="${currentPreviewSvcInfo.appSvcPrincipalOfficersDtoList.size() > 1}"> ${status.index+1}</c:if></strong>:
                            </strong>
                        </p>
                        <%@include file="viewPersonnelDetail.jsp" %>
                    </c:forEach>

                    <c:forEach var="person" items="${currentPreviewSvcInfo.appSvcNomineeDtoList}" varStatus="status">
                        <c:set var="oldPerson"
                               value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcNomineeDtoList[status.index]}"/>
                        <p class="col-xs-12">
                            <strong>
                                Nominee<c:if
                                    test="${currentPreviewSvcInfo.appSvcNomineeDtoList.size() > 1}"> ${status.index+1}</c:if></strong>:
                            </strong>
                        </p>
                        <%@include file="viewPersonnelDetail.jsp" %>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>