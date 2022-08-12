<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach items="${currentPreviewSvcInfo.appSvcSpecialServiceInfoList}" var="appSvcSpecialServiceInfo" varStatus="status">
            <iais:row>
                <div class="col-xs-12 app-title">
                    <p><c:out value="${appSvcSpecialServiceInfo.premName}"/></p>
                    <p>Address: <c:out value="${appSvcSpecialServiceInfo.premAddress}"/></p>
                </div>
            </iais:row>

            <c:forEach var="specialServiceSectionDto" items="${appSvcSpecialServiceInfo.specialServiceSectionDtoList}" varStatus="subSvcRelStatus">

                <iais:row>
                    <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                        <p><strong><c:out value="${specialServiceSectionDto.svcName}"/></strong></p>
                    </div>
                </iais:row>

                <c:choose>
                    <c:when test="${specialServiceSectionDto.appSvcDirectorDtoList != null && specialServiceSectionDto.appSvcDirectorDtoList.size()>1}">
                        <c:set var="DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="DirectorDtoListLength" value="1"/>
                    </c:otherwise>
                </c:choose>
                <c:forEach begin="0" end="${DirectorDtoListLength - 1}" step="1" varStatus="direStatus">
                    <c:set var="index" value="${direStatus.index}"/>
                    <c:set var="appSvcPersonnelDto" value="${specialServiceSectionDto.appSvcDirectorDtoList[index]}"/>
                    <c:set var="title" value="Emergency Department Director"/>
                    <%@include file="viewSpecialServicesFromDetail.jsp"%>
                </c:forEach>

                <c:choose>
                    <c:when test="${specialServiceSectionDto.appSvcChargedNurseDtoList != null && specialServiceSectionDto.appSvcChargedNurseDtoList.size()>1}">
                        <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcChargedNurseDtoList.size()}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="NurseDtoListLength" value="1"/>
                    </c:otherwise>
                </c:choose>
                <c:forEach begin="0" end="${NurseDtoListLength - 1}" step="1" varStatus="nurStatus">
                    <c:set var="index" value="${nurStatus.index}"/>
                    <c:set var="appSvcPersonnelDto" value="${specialServiceSectionDto.appSvcChargedNurseDtoList[index]}"/>
                    <c:set var="title" value="Emergency Department Nurse-in-charge"/>
                    <%@include file="viewSpecialServicesFromDetail.jsp"%>
                </c:forEach>
            </c:forEach>
        </c:forEach>
    </div>
</div>
