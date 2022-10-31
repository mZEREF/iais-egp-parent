<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amend-preview-info form-horizontal min-row">
    <iais:row>
        <div class="col-xs-12">
            <p class="app-title"><c:out value="${currStepName}"/></p>
        </div>
    </iais:row>

    <c:set var="appSvcSuplmFormList" value="${currentPreviewSvcInfo.appSvcSuplmFormList}"/>
    <c:forEach var="appSvcSuplmFormDto" items="${appSvcSuplmFormList}">
        <iais:row>
            <div class="col-xs-12">
                <div class="app-title">${appSvcSuplmFormDto.premName}</div>
                <p class="font-18 bold">Address: ${appSvcSuplmFormDto.premAddress}</p>
            </div>
        </iais:row>
        <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
            <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>
            <c:if test="${batchSize > 0}">
                <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
                <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
                    <c:if test="${item.display}">
                        <%@ include file="viewItem.jsp" %>
                    </c:if>
                </c:forEach>
            </c:if>
        </c:forEach>
    </c:forEach>
</div>

<script>
    $(function () {
        $('.mandatory').hide()
    })
</script>