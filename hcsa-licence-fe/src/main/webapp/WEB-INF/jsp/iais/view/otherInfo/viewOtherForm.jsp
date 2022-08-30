<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amend-preview-info form-horizontal min-row">
    <c:set var="appSvcSuplmFormDto" value="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcSuplmFormDto}"/>
    <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
        <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>
        <c:if test="${batchSize > 0}">
            <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
            <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
                <%@ include file="/WEB-INF/jsp/iais/view/supplementaryForm/viewItem.jsp" %>
            </c:forEach>
        </c:if>
    </c:forEach>
</div>