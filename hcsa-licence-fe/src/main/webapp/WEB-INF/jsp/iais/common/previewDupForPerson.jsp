<c:choose>
    <c:when test="${'1' == config.dupForPerson}">
        <!--CGO -->
        <c:set var="psnList" value="${GovernanceOfficersList}"/>
    </c:when>
    <c:when test="${'2' == config.dupForPerson}">
        <!--PO -->
        <c:set var="psnList" value="${ReloadPrincipalOfficers}"/>
    </c:when>
    <c:when test="${'4' == config.dupForPerson}">
        <!--DPO -->
        <c:set var="psnList" value="${ReloadDeputyPrincipalOfficers}"/>
    </c:when>
    <c:when test="${'8' == config.dupForPerson}">
        <!--MAP -->
        <c:set var="psnList" value="${AppSvcMedAlertPsn}"/>
    </c:when>
    <%--<c:when test="${'16' == config.dupForPerson}">
        <!--SVCPSN -->
        <c:set var="psnList" value="${AppSvcPersonnelDtoList}"/>
    </c:when>--%>
</c:choose>

<c:choose>
    <c:when test="${empty psnList || psnList.size() == 0}">
        <c:set var="psnLength" value="0"/>
    </c:when>
    <c:otherwise>
        <c:set var="psnLength" value="${psnList.size()-1}"/>
    </c:otherwise>
</c:choose>
<c:forEach begin="0" end="${psnLength}" varStatus="psnStat">
    <c:set var="psn" value="${psnList[psnStat.index]}"/>
    <c:set var="mapKey" value="${premIndexNo}${config.id}${psn.cgoIndexNo}"/>
    <c:set var="fileList" value="${reloadMap[mapKey]}"/>
    <%@include file="previewSvcDocContent.jsp"%>
</c:forEach>
