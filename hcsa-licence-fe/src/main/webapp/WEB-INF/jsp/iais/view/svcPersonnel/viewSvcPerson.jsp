<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<div class="amended-service-info-gp">
<label class="app-title">${currStepName}</label>
<div class="amend-preview-info form-horizontal min-row">
<c:choose>
    <c:when test="${'BLB' ==currentSvcCode}">
        <h4>The blood donation centre and/or mobile donation drive is/are under the supervision of</h4>
    </c:when>
    <c:when test="${'TSB' ==currentSvcCode}">
        <strong style="font-size: 20px;">Laboratory Director (Cord Blood Banking Service)</strong>
        <h4><iais:message key="NEW_ACK023"/></h4>
    </c:when>
    <c:when test="${'NMI' ==currentSvcCode}">
        <h4>Please appoint at least one person for each role listed under "Service Personnel".</h4>
    </c:when>
    <c:when test="${'NMA' ==currentSvcCode}">
        <h4>The Nuclear Medicine Assay Service have the following personnel that satisfy the minimum
            requirements at all times</h4>
    </c:when>
</c:choose>

<c:choose>
    <c:when test="${currentPreviewSvcInfo.svcPersonnelDto != null && currentPreviewSvcInfo.svcPersonnelDto.arPractitionerList.size()>0}">
        <c:set var="arPractitionerCount" value="${currentPreviewSvcInfo.svcPersonnelDto.arPractitionerList.size()}"/>
    </c:when>
    <c:otherwise>
        <c:set var="arPractitionerCount" value="0"/>
    </c:otherwise>
</c:choose>


<c:choose>
    <c:when test="${currentPreviewSvcInfo.svcPersonnelDto != null && currentPreviewSvcInfo.svcPersonnelDto.nurseList.size()>0}">
        <c:set var="nurseCount" value="${currentPreviewSvcInfo.svcPersonnelDto.nurseList.size()}"/>
    </c:when>
    <c:otherwise>
        <c:set var="nurseCount" value="0"/>
    </c:otherwise>
</c:choose>


<c:choose>
    <c:when test="${currentPreviewSvcInfo.svcPersonnelDto != null && currentPreviewSvcInfo.svcPersonnelDto.embryologistList.size()>0}">
        <c:set var="embryologistMinCount" value="${currentPreviewSvcInfo.svcPersonnelDto.embryologistList.size()}"/>
    </c:when>
    <c:otherwise>
        <c:set var="embryologistMinCount" value="0"/>
    </c:otherwise>
</c:choose>


<c:choose>
    <c:when test="${currentPreviewSvcInfo.svcPersonnelDto != null && currentPreviewSvcInfo.svcPersonnelDto.specialList.size()>0}">
        <c:set var="specialCount" value="${currentPreviewSvcInfo.svcPersonnelDto.specialList.size()}"/>
    </c:when>
    <c:otherwise>
        <c:set var="specialCount" value="0"/>
    </c:otherwise>
</c:choose>


<c:choose>
    <c:when test="${currentPreviewSvcInfo.svcPersonnelDto != null && currentPreviewSvcInfo.svcPersonnelDto.normalList.size()>0}">
        <c:set var="normalCount" value="${currentPreviewSvcInfo.svcPersonnelDto.normalList.size()}"/>
    </c:when>
    <c:otherwise>
        <c:set var="normalCount" value="0"/>
    </c:otherwise>
</c:choose>



<c:if test="${arPractitionerCount != 0}">
    <div class="panel-main-content">
        <c:forEach begin="0" end="${arPractitionerCount - 1}" step="1" varStatus="status">
            <c:set var="index" value="${status.index}"/>
            <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.arPractitionerList[index]}"/>
            <%@include file="servicePersonnelArDetail.jsp" %>
        </c:forEach>
    </div>
    </div>

</c:if>

<c:if test="${nurseCount != 0}">
    <div class="panel-main-content">
        <c:forEach begin="0" end="${nurseCount - 1}" step="1" varStatus="status">
            <c:set var="index" value="${status.index}"/>
            <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.nurseList[index]}"/>
            <%@include file="servicePersonnelNurse.jsp" %>
        </c:forEach>
    </div>
    </div>
</c:if>

<c:if test="${embryologistMinCount != 0}">
    <div class="panel-main-content">
        <c:forEach begin="0" end="${embryologistMinCount - 1}" step="1" varStatus="status">
            <c:set var="index" value="${status.index}"/>
            <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.embryologistList[index]}"/>
            <%@include file="servicePersonnelEmbryologist.jsp" %>
        </c:forEach>
    </div>
    </div>
</c:if>


<c:if test="${specialCount != 0}">
    <c:forEach begin="0" end="${specialCount - 1}" step="1" varStatus="status">
        <c:set var="index" value="${status.index}"/>
        <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.specialList[index]}"/>
        <%@include file="servicePersonnelDetail.jsp" %>
    </c:forEach>
</c:if>


<c:if test="${normalCount != 0}">
    <div class="panel-main-content">
        <c:forEach begin="0" end="${normalCount - 1}" step="1" varStatus="status">
            <c:set var="index" value="${status.index}"/>
            <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.normalList[index]}"/>
            <%@include file="servicePersonnelBlood.jsp" %>
        </c:forEach>
    </div>
</c:if>



</div>


</div>