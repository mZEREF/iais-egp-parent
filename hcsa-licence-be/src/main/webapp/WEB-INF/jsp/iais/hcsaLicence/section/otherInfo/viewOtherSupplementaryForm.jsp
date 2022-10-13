<c:set var="appSvcSuplmFormDto" value="${otherInfo.appSvcSuplmFormDto}"/>
<c:set var="oldAppSvcSuplmFormDto" value="${oldOtherInfo.appSvcSuplmFormDto}"/>
<c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
    <c:set var="oldAppSvcSuplmGroupDto" value="${oldAppSvcSuplmFormDto.appSvcSuplmGroupDtoList[status.index]}"/>
    <table class="col-xs-12">
        <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>
        <c:if test="${batchSize > 0}">
            <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
                <c:set var="oldItem" value="${oldAppSvcSuplmGroupDto.appSvcSuplmItemDtoList[statuss.index]}"/>
                <c:if test="${item.display}">
                    <%@ include file="/WEB-INF/jsp/iais/hcsaLicence/section/supplementaryForm/viewItem.jsp" %>
                </c:if>
            </c:forEach>
        </c:if>
    </table>
</c:forEach>