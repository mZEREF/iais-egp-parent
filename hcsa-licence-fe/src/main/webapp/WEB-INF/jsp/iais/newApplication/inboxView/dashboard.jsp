<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot
            =IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" id="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="../../common/dashboardDropDown.jsp" %>
                </div>
            </div>
        </div>
    </div>
</div>