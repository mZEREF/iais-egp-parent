<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="appType" value="${AppSubmissionDto.appType}" />
<div class="dashboard" id="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="../../common/dashboardDropDown.jsp" %>
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <c:choose>
                            <c:when test="${'APTY004' == appType}">
                                <%@include file="../../withoutrenewal/renewalHeader.jsp"%>
                            </c:when>
                            <c:when test="${'APTY005' == appType}">
                                <%@include file="../../resForChange/amendHeader.jsp"%>
                            </c:when>
                            <c:when test="${'APTY004'==APPLICATION_TYPE}">
                                <h1>Licence Renewal</h1>
                                <h3 id="newSvc">
                                    You are renewing for
                                    <c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status">
                                        <strong>${hcsaServiceDto.svcName}</strong>
                                        <c:if test="${!status.last}"> | </c:if>
                                    </c:forEach>
                                </h3>
                            </c:when>
                            <c:when test="${'APTY005'==APPLICATION_TYPE}">
                              <h1>Amendment</h1>
                                <h3 id="newSvc">
                                    You are amending for
                                    <c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status">
                                        <strong>${hcsaServiceDto.svcName}</strong>
                                        <c:if test="${!status.last}"> | </c:if>
                                    </c:forEach>
                                </h3>
                            </c:when>
                            <c:otherwise>
                                <h1>New Licence Application</h1>
                                <h3 id="newSvc">
                                    You are applying for
                                    <c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status">
                                        <strong>${hcsaServiceDto.svcName}</strong>
                                        <c:if test="${!status.last}"> | </c:if>
                                    </c:forEach>
                                </h3>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .mandatory{
        color: rgb(255,0,0);
    }
    .app-font-size-16{
        font-size: 16px;
    }
</style>