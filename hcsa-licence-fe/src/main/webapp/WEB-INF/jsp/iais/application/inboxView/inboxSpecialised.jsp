<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="specialisedTitle"><iais:message key="GENERAL_TITLE01" escape="false"/></c:set>
<c:set var="appPremSpecialisedDtoList" value="${AppSubmissionDto.appPremSpecialisedDtoList}" />
<c:forEach var="specialised" items="${appPremSpecialisedDtoList}">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewSpecialised" role="button" aria-expanded="true">
                    <%--Category/Discipline & Specialised Service/Specified Test--%>
                    ${specialisedTitle} - ${specialised.baseSvcName}
                </a>
            </h4>
        </div>
        <div id="previewSpecialised" class="panel-collapse collapse ">
            <div class="panel-body">
                <div class="panel-main-content form-horizontal min-row">
                    <%@include file="/WEB-INF/jsp/iais/view/specialised/viewSpecialisedContent.jsp"%>
                </div>
            </div>
        </div>
    </div>
</c:forEach>
