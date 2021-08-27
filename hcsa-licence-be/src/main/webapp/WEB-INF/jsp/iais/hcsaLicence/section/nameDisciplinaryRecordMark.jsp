<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="profRegNo" value="${param.profRegNo}" />
<c:set var="personName" value="${param.personName}" />
<c:set var="methodName" value="${param.methodName}" />

<c:if test="${not empty proHashMap[profRegNo]}">
    <c:if test="${proHashMap[profRegNo].name==personName}">
        <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
    </c:if>
    <c:if test="${proHashMap[profRegNo].name!=personName}">
        <img src="/hcsa-licence-web/img/2020109171436.png" onclick="javascript:${methodName}(this);" width="25" height="25" alt="NETS">
    </c:if>
</c:if>
