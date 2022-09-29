<c:forEach var="appSvcSuplmGroupDto" items="${otherInfo.appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
    <table class="col-xs-12">
        <c:set var="oldAppSvcSuplmGroupDto" value="${oldOtherInfo.appSvcSuplmFormDto.appSvcSuplmGroupDtoList[status.index]}"/>
        <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>
        <c:if test="${batchSize > 0}">
            <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="statuss">
                <c:set var="oldItem" value="${oldAppSvcSuplmFormDto.appSvcSuplmGroupDtoList[statuss.index]}"/>
                <c:if test="${item.display}">
                    <%@ include file="/WEB-INF/jsp/iais/hcsaLicence/section/supplementaryForm/viewItem.jsp" %>
                </c:if>
            </c:forEach>
        </c:if>
    </table>
</c:forEach>
<script>
    $(function () {
        $('.mandatory').hide()
    })
</script>