<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="svc-title">${currStepName}</label>
    <div class="amend-preview-info">
        <p></p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <table aria-describedby="" class="col-xs-12">
                        <thead style="display: none">
                        <tr><th scope="col"></th> </tr>
                        </thead>
                        <c:set var="reloadMap" value="${currentPreviewSvcInfo.multipleSvcDoc}"/>
                        <c:forEach var="config" items="${svcDocConfig}" varStatus="configStat">
                            <c:choose>
                                <c:when test="${'0' == config.dupForPrem}">
                                    <c:choose>
                                        <c:when test="${empty config.dupForPerson}">
                                            <c:set var="fileList" value="${reloadMap[config.id]}"/>
                                            <%@include file="previewSvcDocContent.jsp"%>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="premIndexNo" value=""/>
                                            <%@include file="previewDupForPerson.jsp"%>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:when test="${'1' == config.dupForPrem}">
                                    <c:forEach var="prem" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="premStat">
                                        <c:set var="premIndexNo" value="${prem.premisesIndexNo}"/>
                                        <c:choose>
                                            <c:when test="${empty config.dupForPerson}">
                                                <c:set var="mapKey" value="${premIndexNo}${config.id}"/>
                                                <c:set var="fileList" value="${reloadMap[mapKey]}"/>
                                                <%@include file="previewSvcDocContent.jsp"%>
                                            </c:when>
                                            <c:otherwise>
                                                <%@include file="previewDupForPerson.jsp"%>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

