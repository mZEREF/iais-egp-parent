<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <%@include file="../../inbox/dashboard/menuPage.jsp" %>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="dashboard-page-title">
                    <h1>Application For Facility Renewal</h1>
                </div>
                <p>You are renewing the Approved Facility <strong>(Approval No.<c:out value="${instructionInfo.approvalNo}"/>)</strong></p>
            </div>
        </div>
    </div>
</div>