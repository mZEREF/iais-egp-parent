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
                <div style="margin-top: 20px;margin-left: 2%" >
                    <h1>List of Rectifications</h1>
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