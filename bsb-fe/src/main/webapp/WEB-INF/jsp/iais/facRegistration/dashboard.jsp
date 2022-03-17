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
                    <h1>New Facility Registration</h1>
                    <p>You are applying for <strong>Facility Classification</strong> with activity type:</p>
                    <p><strong>Facility Activity Type</strong></p>
                </div>
            </div>
        </div>
    </div>
</div>