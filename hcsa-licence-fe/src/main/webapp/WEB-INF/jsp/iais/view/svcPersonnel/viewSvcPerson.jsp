<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <c:choose>
        <c:when test="${currentPreviewSvcInfo.svcPersonnelDto != null && currentPreviewSvcInfo.svcPersonnelDto.arPractitionerList.size()>0}">
            <c:set var="arPractitionerCount"
                   value="${currentPreviewSvcInfo.svcPersonnelDto.arPractitionerList.size()}"/>
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
            <c:set var="embryologistMinCount"
                   value="${currentPreviewSvcInfo.svcPersonnelDto.embryologistList.size()}"/>
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
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:if test="${arPractitionerCount != 0}">
            <c:forEach begin="0" end="${arPractitionerCount - 1}" step="1" varStatus="status">
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto"
                       value="${currentPreviewSvcInfo.svcPersonnelDto.arPractitionerList[index]}"/>
                <iais:row>
                    <div class="col-xs-12">
                        <p><strong>AR Practitioner<c:if test="${arPractitionerCount > 1}"> ${index+1}</c:if>:</strong>
                        </p>
                    </div>
                </iais:row>
                <%@include file="viewServicePersonnelArDetail.jsp" %>
            </c:forEach>

            <%--    TODO --%>
<%--            <iais:row>--%>
<%--                <iais:field width="5" value="Total Number of AR Practitioner"/>--%>
<%--                <iais:value width="7" cssClass="col-md-7" display="true">--%>
<%--                    <c:out value="0"/>--%>
<%--                    &lt;%&ndash;                    <span id="arNumber">0<span>&ndash;%&gt;--%>
<%--                </iais:value>--%>
<%--            </iais:row>--%>

        </c:if>

        <c:if test="${nurseCount != 0}">
            <c:forEach begin="0" end="${nurseCount - 1}" step="1" varStatus="status">
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.nurseList[index]}"/>
                <iais:row>
                    <div class="col-xs-12">
                        <p><strong>Nurse<c:if test="${nurseCount > 1}"> ${index+1}</c:if>:</strong></p>
                    </div>
                </iais:row>
                <%@include file="viewServicePersonnelNurse.jsp" %>
            </c:forEach>
        </c:if>

        <c:if test="${embryologistMinCount != 0}">
            <c:forEach begin="0" end="${embryologistMinCount - 1}" step="1" varStatus="status">
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto"
                       value="${currentPreviewSvcInfo.svcPersonnelDto.embryologistList[index]}"/>
                <iais:row>
                    <div class="col-xs-12">
                        <p><strong>Embryologist<c:if test="${embryologistMinCount > 1}"> ${index+1}</c:if>:</strong></p>
                    </div>
                </iais:row>
                <%@include file="viewServicePersonnelEmbryologist.jsp" %>
            </c:forEach>
        </c:if>

        <c:if test="${normalCount != 0}">
            <c:forEach begin="0" end="${normalCount - 1}" step="1" varStatus="status">
                <c:set var="index" value="${status.index}"/>
                <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.normalList[index]}"/>
                <iais:row>
                    <div class="col-xs-12">
                        <p><strong>Service Personnel<c:if test="${normalCount > 1}"> ${index+1}</c:if>:</strong></p>
                    </div>
                </iais:row>
                <%@include file="viewServicePersonnelBlood.jsp" %>
            </c:forEach>
        </c:if>

<%--        <c:if test="${specialCount != 0}">
            <c:forEach begin="0" end="${specialCount - 1}" step="1" varStatus="status">
                <c:set var="index" value="${status.index}"/>
                <c:set value="111" var="logo"/>
                <c:set var="appSvcPersonnelDto"
                       value="${currentPreviewSvcInfo.svcPersonnelDto.specialList[index]}"/>
                <iais:row>
                    <div class="col-xs-12">
                        <p><strong>Service Personnel<c:if test="${specialCount > 1}"> ${index+1}</c:if>:</strong></p>
                    </div>
                </iais:row>
                <%@include file="viewServicePersonnelDetail.jsp" %>
            </c:forEach>
        </c:if>--%>

    </div>
</div>


