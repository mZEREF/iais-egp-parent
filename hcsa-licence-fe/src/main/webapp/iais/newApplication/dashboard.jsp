<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="../common/dashboardDropDown.jsp" %>
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <c:choose>
                            <c:when test="${'APTY004' == AppSubmissionDto.appType}">
                                <h1>Renewal Licence Application</h1>
                            </c:when>
                            <c:when test="${'APTY005' == AppSubmissionDto.appType}">
                                <h1>Amend Licence Application</h1>
                            </c:when>
                            
                            <c:otherwise>
                                <h1>New Licence Application</h1>
                            </c:otherwise>
                        </c:choose>
                        <h3>
                            You are applying for
                            <c:forEach var="hcsaServiceDto" items="${hcsaServiceDtoList}" varStatus="status">
                                <strong>${hcsaServiceDto.svcName}</strong>
                                <c:if test="${!status.last}"> | </c:if>
                            </c:forEach>
                        </h3>
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

</style>