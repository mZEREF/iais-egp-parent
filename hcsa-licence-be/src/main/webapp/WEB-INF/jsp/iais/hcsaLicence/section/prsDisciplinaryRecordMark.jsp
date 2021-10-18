<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="profRegNo" value="${param.profRegNo}" />
<c:set var="methodName" value="${param.methodName}" />

<c:if test="${not empty profRegNo}">
    <c:if test="${ empty listHashMap[profRegNo]}">
        <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
    </c:if>
    <c:if test="${not empty listHashMap[profRegNo]}">
        <img src="/hcsa-licence-web/img/2020109171436.png" onclick="javascript:${methodName}(this);" width="25" height="25" alt="NETS">
    </c:if>
</c:if>