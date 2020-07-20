<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="../common/dashboardDropDown.jsp" %>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="prelogin-title">
                <h1>Withdrawal Form</h1>
            </div>
        </div>
    </div>
</div>

<style>

    .prelogin-title{
        padding-left: 90px;
    }

</style>