<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="dashboard" id="comDashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
    <div class="container">
        <div class="navigation-gp">
            <c:if test="${showUserMenu}">
                <div class="row">
                    <%@ include file="../common/dashboardDropDown.jsp" %>
                </div>
            </c:if>
            <div class="row">
                <c:choose>
                    <c:when test="${DashboardTitle != null && DashboardTitle !=''}">
                        <div class="col-xs-12"><h1>${DashboardTitle}</h1></div>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${'APTY005' == AppSubmissionDto.appType}">
                                <%@include file="../resForChange/amendHeaderNotLic.jsp"%>
                            </c:when>
                            <c:when test="${'APTY004' == AppSubmissionDto.appType}">
                                <%@include file="../withoutrenewal/renewalHeader.jsp"%>
                            </c:when>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<style>
    .mandatory{
        color: rgb(255,0,0);
    }

</style>

<script>

    //todo: current rfc need change interval
    <c:choose>
        <c:when test="${DashboardTitle != null && DashboardTitle !=''}">
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${'APTY005' == AppSubmissionDto.appType}">
                    $('#comDashboard').css('padding-bottom','0px');
                </c:when>

            </c:choose>
        </c:otherwise>
    </c:choose>

</script>