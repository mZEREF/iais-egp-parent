<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<input type="hidden" name="specialised_svc_code" value="${specialised_svc_code}">

<c:set var="specialisedTitle"><iais:message key="GENERAL_TITLE01" escape="false"/></c:set>
<c:forEach var="specialised" items="${AppSubmissionDto.appPremSpecialisedDtoList}" varStatus="vs">
    <c:if test="${empty printView && (!FirstView || needShowErr)}">
        <c:set var="headingSign" value="${fn:contains(coMap.multiSpecialised, specialised.baseSvcCode) ? 'incompleted' : 'completed'}" />
    </c:if>
    <div class="panel panel-default">
        <div class="panel-heading ${headingSign}">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewSpecialised${vs.index}" role="button" aria-expanded="true">
                    ${specialisedTitle}<c:if test="${!withoutSvcName}"> - ${specialised.baseSvcName}</c:if>
                </a>
            </h4>
        </div>
        <div id="previewSpecialised${vs.index}" class="panel-collapse collapse previewSpecialised <c:if test="${!empty printFlag}">in</c:if>">
            <div class="panel-body">
                <c:if test="${(empty AppSubmissionDto.appEditSelectDto || AppSubmissionDto.appEditSelectDto.specialisedEdit)
                        && empty printView && (empty isSingle || isSingle == 'Y')}">
                    <p><div class="text-right app-font-size-16"><a href="#" class="specialisedEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
                </c:if>
                <div class="panel-main-content form-horizontal min-row">
                    <%@include file="specialised/viewSpecialisedContent.jsp"%>
                </div>
            </div>
        </div>
    </div>
</c:forEach>