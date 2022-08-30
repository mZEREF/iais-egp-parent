<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit"
       value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<c:set var="appSvcSuplmFormDto" value="${currSvcInfoDto.appSvcOtherInfoDto.appSvcSuplmFormDto}"/>
<c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}">
    <c:set var="count" value="${appSvcSuplmGroupDto.count}"/>
    <c:set var="baseSize" value="${appSvcSuplmGroupDto.baseSize}"/>
    <c:if test="${count > 0}">
        <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
        <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
            <%@include file="otherItem.jsp"%>
        </c:forEach>
    </c:if>
</c:forEach>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/iais/application/serviceInfo/supplementaryForm/itemFun.jsp" %>
<script type="text/javascript">
    $(function () {

    });

</script>
