<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<c:if test="${'1' == appSvcOtherInfoDto.provideTop}">
    <c:set var="appSvcSuplmFormDto" value="${appSvcOtherInfoDto.appSvcSuplmFormDto}"/>
    <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
        <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>
        <c:if test="${batchSize > 0}">
            <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
            <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
                <c:if test="${item.display}">
                    <%@ include file="/WEB-INF/jsp/iais/application/view/supplementaryForm/viewItem.jsp" %>
                </c:if>
            </c:forEach>
        </c:if>
    </c:forEach>
</c:if>