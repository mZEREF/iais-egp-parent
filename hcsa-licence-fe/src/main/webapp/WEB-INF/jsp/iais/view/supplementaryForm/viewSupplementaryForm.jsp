<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="row form-horizontal min-row">
    <iais:row>
        <div class="col-xs-12">
            <p class="app-title"><c:out value="${currStepName}"/></p>
        </div>
    </iais:row>

    <c:set var="appSvcSuplmFormDto" value="${currSvcInfoDto.appSvcSuplmFormDto}"/>
    <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}" varStatus="status">
        <c:set var="batchSize" value="${appSvcSuplmGroupDto.count}"/>
        <c:if test="${batchSize > 0}">
            <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
            <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
                <%@ include file="viewItem.jsp" %>
            </c:forEach>
        </c:if>
    </c:forEach>
</div>