<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status" >
    <c:set var="specialised_svc_code" value="${hcsaServiceDto.svcCode}" />
    <c:if test="${empty printView}">
        <c:choose>
            <c:when test="${!FirstView}">
                <c:set var="headingSign" value="${fn:contains(serviceConfig, specialised_svc_code) ? 'incompleted' : 'completed'}" />
            </c:when>
            <c:when test="${needShowErr}">
                <c:set var="headingSign" value="${fn:contains(serviceConfig, specialised_svc_code) ? 'incompleted' : 'completed'}" />
            </c:when>
        </c:choose>
    </c:if>

    <c:forEach var="specialised" items="${AppSubmissionDto.appPremSpecialisedDtoList}">
        <c:if test="${specialised_svc_code == specialised.baseSvcCode}">
            <c:if test="${empty categorySectionName}"><c:set var="categorySectionName" value="${specialised.categorySectionName}" /></c:if>
            <c:if test="${empty specialSvcSecName}"><c:set var="specialSvcSecName" value="${specialised.specialSvcSecName}" /></c:if>
        </c:if>
    </c:forEach>
    <c:set var="specialisedTitle">${categorySectionName}<c:if test="${not empty categorySectionName}"> & </c:if>${specialSvcSecName}</c:set>
    <div class="panel panel-default">
        <div class="panel-heading ${headingSign}">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewSpecialised${status.index}" role="button" aria-expanded="true">
                    ${specialisedTitle} - ${hcsaServiceDto.svcName}
                </a>
            </h4>
        </div>
        <div id="previewSpecialised${status.index}" class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>">
            <div class="panel-body">
                <c:if test="${(empty AppSubmissionDto.appEditSelectDto || AppSubmissionDto.appEditSelectDto.specialisedEdit)
                && empty printView && (empty isSingle || isSingle == 'Y')}">
                    <p><div class="text-right app-font-size-16"><a href="#" id="subLicenseeEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
                </c:if>
                <div class="panel-main-content form-horizontal min-row">
                    <%@include file="specialised/viewSpecialisedContent.jsp"%>
                </div>
            </div>
        </div>
    </div>
</c:forEach>