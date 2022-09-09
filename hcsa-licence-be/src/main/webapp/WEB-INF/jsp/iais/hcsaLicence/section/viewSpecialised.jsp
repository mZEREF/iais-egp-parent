<c:set var="specialisedTitle"><iais:message key="GENERAL_TITLE01" escape="false"/></c:set>
<c:forEach var="specialised" items="${appSubmissionDto.appPremSpecialisedDtoList}" varStatus="vs">
    <c:set var="oldSpecialised" value="${appSubmissionDto.oldAppSubmissionDto.appPremSpecialisedDtoList[status.index]}"/>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#previewSpecialised${vs.index}" role="button" aria-expanded="true">
                    ${specialisedTitle} - ${specialised.baseSvcName}
                </a>
            </h4>
        </div>
        <div id="previewSpecialised${vs.index}" class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>">
            <div class="panel-body">
                <p class="text-right">
                    <c:if test="${rfi == 'rfi'}">
                        <c:if test="${(appEdit.premisesEdit || appEdit.premisesListEdit)&& canEidtPremise }">
                            <input class="form-check-input" id="premisesCheckbox" type="checkbox"
                                   name="editCheckbox" <c:if test="${pageEdit.premisesEdit}">checked</c:if> aria-invalid="false" value="premises">
                        </c:if>
                    </c:if>
                </p>
                <div class="panel-main-content form-horizontal min-row">
                    <%@include file="specialised/viewSpecialisedContent.jsp"%>
                </div>
            </div>
        </div>
    </div>
</c:forEach>