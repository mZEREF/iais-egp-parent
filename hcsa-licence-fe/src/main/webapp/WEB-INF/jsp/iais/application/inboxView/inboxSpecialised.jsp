<c:forEach var="specialised" items="${AppSubmissionDto.appPremSpecialisedDtoList}">
    <c:if test="${empty categorySectionName}"><c:set var="categorySectionName" value="${specialised.categorySectionName}"/></c:if>
    <c:if test="${empty specialSvcSecName}"><c:set var="specialSvcSecName" value="${specialised.specialSvcSecName}"/></c:if>
</c:forEach>
<c:set var="specialisedTitle">${categorySectionName}<c:if test="${not empty categorySectionName}"> & </c:if>${specialSvcSecName}</c:set>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#previewSpecialised" role="button" aria-expanded="true">
                <%--Category/Discipline & Specialised Service/Specified Test--%>
                ${specialisedTitle} - ${hcsaServiceDto.svcName}
            </a>
        </h4>
    </div>
    <div id="previewSpecialised" class="panel-collapse collapse ">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal min-row">
                <jsp:include page="/WEB-INF/jsp/iais/view/specialised/viewSpecialisedContent.jsp"/>
            </div>
        </div>
    </div>
</div>