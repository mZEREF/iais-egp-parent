<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="dashboard" id="comDashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
    <div class="container">
        <div class="navigation-gp">
            <c:if test="${appeal!='appeal'}">
                <div class="row">
                    <%@ include file="./dashboardDropDown.jsp" %>
                </div>
            </c:if>
            <div class="row">
                <c:choose>
                    <c:when test="${'retriggerGiro' == DashboardTitle}">
                        <div class="col-xs-12">
                            <div class="tab-gp steps-tab">
                                <div class="tab-content">
                                    <h1>${title}</h1>
                                    <c:if test="${empty renewAck}">
                                        <p>${smallTitle}</p>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${'empty' == DashboardTitle }">
                        <div class="col-xs-12"></div>
                    </c:when>
                    <c:when test="${DashboardTitle != null && DashboardTitle !=''}">
                        <div class=" steps-tab">
                            <div class="tab-content">
                                <div class="tab-pane active" id="previewTab" role="tabpanel">
                                    <h1 class="font-weight 0">${DashboardTitle}</h1>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${'APTY005' == AppSubmissionDto.appType}">
                                <%@include file="../resForChange/amendHeader.jsp"%>
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