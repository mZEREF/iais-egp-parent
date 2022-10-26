<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <%@include file="../inbox/dashboard/menuPage.jsp" %>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-page-title">
                    <h1>New Reportable Event</h1>
                    <c:if test="${incidentTitle ne null}">
                        <h3 style="font-weight: normal">You are submitting for <iais:code code="${incidentTitle}"/></h3>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>