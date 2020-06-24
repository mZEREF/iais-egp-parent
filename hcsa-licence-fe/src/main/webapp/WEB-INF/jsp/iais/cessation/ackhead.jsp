<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="dashboard" id="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="../common/dashboardDropDown.jsp" %>
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <div class="col-xs-12">
                            <h1 class="font-weight 0">Acknowledgement</h1>
                        </div>
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